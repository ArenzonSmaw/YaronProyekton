package deliveryTrio.YaronProjecton.services;

import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.dataSavingProblems.CantAccessDataException;
import deliveryTrio.YaronProjecton.exceptions.dataSavingProblems.CantUpdateDataException;
import deliveryTrio.YaronProjecton.exceptions.HandsFullException;
import deliveryTrio.YaronProjecton.exceptions.InvalidInputException;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.exceptions.UnfinishedDutyException;
import deliveryTrio.YaronProjecton.dataAccess.DataAccessObject;
import deliveryTrio.YaronProjecton.dataAccess.IDAO;
import deliveryTrio.YaronProjecton.dataAccess.delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.dataAccess.deliveryPerson.DeliveryPersonCollection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service("manager")
public class DeliveryManager {
    @NotNull
    private final IDAO dao;
    private int deliveryID, deliveryPersonID;
    private int startingDeliveryID;
    private int startingDeliveryPersonID;
    public DeliveryManager(DataAccessObject accessObj, @Value("${manager.startingDeliveryID}") int startingDeliveryID, @Value("${manager.startingDeliveryPersonID}") int startingDeliveryPersonID, @Value("${manager.deliveryPersonsLimit}") int deliveryPersonsLimit) {
        this.dao = accessObj;
        this.startingDeliveryID = startingDeliveryID;
        this.startingDeliveryPersonID = startingDeliveryPersonID;
        DeliveryPerson.defaultMaxCapacity = deliveryPersonsLimit;
    }
    @PostConstruct
    public void loadRunningIDs() {
        try {
            deliveryID = ((Delivery)dao.getAllDeliveries().getList().getLast()).getID() + 1;
        } catch (NotFoundException e) {
            deliveryID = startingDeliveryID; //base id
        }
        try {
            deliveryPersonID = ((DeliveryPerson)dao.getAllDeliveryPersons().getList().getLast()).getID() + 1;
        } catch (NotFoundException e) {
            deliveryPersonID = startingDeliveryPersonID; // base id
        }
    }

    private boolean assignDelivery(Delivery delivery) throws HandsFullException, NotFoundException, NoAvailableDeliveryPersonException {
        ArrayList<DeliveryPerson> lst = dao.getAllDeliveryPersons().getList();
        DeliveryPerson temp;
        if (lst.isEmpty())
            throw new NoAvailableDeliveryPersonException(delivery.getID(), delivery.getDestination());
        else {
            for (int i = 0; i < lst.size(); i++) {
                temp = lst.get(i);
                if (temp.getDeliveryCounter() < temp.getDeliveryMaxCapacity() && temp.getCity().equals(delivery.getDestination())) {
                    temp.addDelivery();
                    delivery.setRef(temp);
                    return true;
                }
            }
        }
        throw new NoAvailableDeliveryPersonException(delivery.getID(), delivery.getDestination());
    }

    public boolean addDeliveryPerson(String name, String city) throws InvalidInputException {
        DeliveryPerson temp = new DeliveryPerson(name, city, deliveryPersonID);

        ValidatorUtil.isValid(temp);

        deliveryPersonID++;
        try {
            dao.add(temp);
        } catch (CantUpdateDataException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }

        assignForgottenDeliveries(); // for the chance new delivery person can handle unassigned deliveries

        return true;
    }
    public boolean addDeliveryPerson(DeliveryPerson deliverer) throws InvalidInputException {
        if (deliverer.getDelivererID() == 0) {
            deliverer.setDelivererID(deliveryPersonID);
            deliveryPersonID++;
        }
        try {
            dao.add(deliverer);
        } catch (CantUpdateDataException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }

        assignForgottenDeliveries(); // for the chance new delivery person can handle unassigned deliveries

        return true;
    }
    public boolean addDelivery(Delivery delivery) throws InvalidInputException, NotFoundException, NoAvailableDeliveryPersonException, CantUpdateDataException, CantAccessDataException{
        System.out.println(delivery);
        if (delivery.getDeliveryNo() == 0) {
            delivery.setDeliveryNo(deliveryID);
            deliveryID++;
        }
        try {
            dao.add(delivery);
            if (delivery.getRef()==null)
                assignDelivery(delivery);

        } catch (HandsFullException e) {
            throw new NoAvailableDeliveryPersonException(deliveryID-1, delivery.getDestination());
        }
        return true;
    }

    public DeliveryCollection getDeliveries() throws NotFoundException{
        return dao.getAllDeliveries();
    }
    public ArrayList<Delivery> getDeliveriesOfPreson(int id) throws NotFoundException{
        ArrayList<Delivery> al = new ArrayList<Delivery>();
        DeliveryPerson person = dao.getDeliveryPersonById(id);
        DeliveryCollection deliveries = dao.getAllDeliveries();
        for (Delivery del : deliveries) {
            if(del.getRef() != null && del.getRef().equals(person))
                al.add(del);
        }
        return al;
    }
    public DeliveryPersonCollection getDeliveryPersons() throws NotFoundException {
        return dao.getAllDeliveryPersons();
    }

    public DeliveryPerson getDeliveryPerson(int id) throws DeliveryPersonNotFoundException{
        return dao.getDeliveryPersonById(id);
    }
    public Delivery getDelivery(int num) throws DeliveryNotFoundException {
        return dao.getDeliveryById(num);
    }

    public void fireDeliveryPerson(int id) throws UnfinishedDutyException, NotFoundException, CantUpdateDataException {
        DeliveryPerson temp = getDeliveryPerson(id);
        if (temp.getDeliveryCounter() > 0) {
            System.out.println(temp.getDeliveryCounter());
            throw new UnfinishedDutyException(id);
        }

        dao.remove(temp);
    }
    public void delivered(int number) throws NotFoundException, CantUpdateDataException {
        Delivery temp = getDelivery(number);
        if(temp.getRef() != null)
            temp.getRef().removeDelivery();
        dao.remove(temp);
    }

    public void modifyDelivery(int num, String field, String newVal) throws HandsFullException, NoAvailableDeliveryPersonException, NotFoundException {
        Delivery temporaryDelivery = getDelivery(num);
        DeliveryPerson previousDeliveryPerson = temporaryDelivery.getRef();
        switch (field) {
            case "destination":
                temporaryDelivery.setDestination(newVal);
                previousDeliveryPerson.removeDelivery();  // decreases deliveryCount
                temporaryDelivery.setRef(null);
                assignDelivery(temporaryDelivery);
                break;
            case "ref":
                try {
                    String lock = previousDeliveryPerson.getCity();
                    previousDeliveryPerson.setCity("administrativeleave");
                    try {
                        assignDelivery(temporaryDelivery);
                    } catch (NoAvailableDeliveryPersonException e) {
                        temporaryDelivery.setRef(previousDeliveryPerson);
                    } finally {
                        previousDeliveryPerson.setCity(lock);
                        if(!temporaryDelivery.getRef().equals(previousDeliveryPerson))
                            previousDeliveryPerson.removeDelivery();
                        else
                            throw new NoAvailableDeliveryPersonException(num,lock);
                    }
                } catch (NullPointerException e) {
                    assignDelivery(temporaryDelivery);
                }
                break;
            case "weight":
                temporaryDelivery.setWeight(Double.parseDouble(newVal));
                break;
            case "customerID":
                temporaryDelivery.setCustomerID(newVal);
                break;
            default:
                throw new RuntimeException("wallahi the field is lo relevanti");

        }
    }
    public void modifyDeliveryPerson(int id, String field, String newVal) throws  UnfinishedDutyException, NotFoundException, InvalidInputException {
        DeliveryPerson temp = getDeliveryPerson(id);
        switch (field) {
            case "city":
                if(temp.getDeliveryCounter() > 0 && newVal != "administrativeleave")
                    throw new UnfinishedDutyException(id);
                temp.setName(newVal);
                break;
            case "name":
                temp.setName(newVal);
                break;
            case "max capacity":
                temp.setMaxCapacity(newVal);
                break;
            default:
                throw new RuntimeException("halas with the fake fields");
        }
        ValidatorUtil.isValid(temp);
    }
    @PreDestroy
    public void assignForgottenDeliveries() {
        try {
            DeliveryCollection deliveries = dao.getAllDeliveries();

            for (Delivery delivery : deliveries) {
                if (delivery.getRef() == null) {
                    try {
                        assignDelivery(delivery);
                    } catch (Exception e) {
                        //Unlucky
                    }
                }
            }
        } catch (NotFoundException e) {
            //at least we tried eh?
        }
    }
}
