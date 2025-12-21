package deliveryTrio.YaronProjecton.Services;

import deliveryTrio.YaronProjecton.Entities.Delivery;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems.CantAccessDataException;
import deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems.CantUpdateDataException;
import deliveryTrio.YaronProjecton.Exceptions.HandsFullException;
import deliveryTrio.YaronProjecton.Exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.NotFoundException;
import deliveryTrio.YaronProjecton.Exceptions.UnfinishedDutyException;
import deliveryTrio.YaronProjecton.Services.DataAccess.DataAccessObject;
import deliveryTrio.YaronProjecton.Services.Delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonCollection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service("manager")
public class DeliveryManager {
    @NotNull
    private final DataAccessObject dao;
    private int deliveryID, deliveryPersonID;
    public DeliveryManager(DataAccessObject accessObj) {
        this.dao = accessObj;
    }
    @PostConstruct
    public void loadRunningIDs() {
        try {
            deliveryID = ((Delivery)dao.getAllDeliveries().getList().getLast()).getID() + 1;
        } catch (NotFoundException e) {
            deliveryID = 1000; //base id
        }
        try {
            deliveryPersonID = ((DeliveryPerson)dao.getAllDeliveryPersons().getList().getLast()).getID() + 1;
        } catch (NotFoundException e) {
            deliveryPersonID = 10000000; // base id
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
                if (temp.getDeliveryCounter() < temp.getDeliveryMaxCapacity()
                        && temp.getCity() == delivery.getDestination()) {
                    temp.addDelivery();
                    delivery.setRef(temp);
                    return true;
                }
            }
        }
        throw new NoAvailableDeliveryPersonException(delivery.getID(), delivery.getDestination());
    }

    public boolean AddDeliveryPerson(String name, String city) {
        DeliveryPerson temp = new DeliveryPerson(name, city, deliveryPersonID);
        deliveryPersonID++;
        try {
            dao.add(temp);
        } catch (CantUpdateDataException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
        try {
            dao.update();
        } catch (CantAccessDataException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean AddDelivery(double weight, String city, String CustID) throws NotFoundException, NoAvailableDeliveryPersonException, CantUpdateDataException, CantAccessDataException{
        Delivery temp = new Delivery(weight, city, CustID, deliveryID);
        deliveryID++;
        try {
            assignDelivery(temp);

            dao.add(temp);

            dao.update();
        } catch (HandsFullException e) {
            throw new NoAvailableDeliveryPersonException(deliveryID-1, city);
        }
        return true;
    }

    public DeliveryCollection getDeliveries() {
        try {
            return dao.getAllDeliveries();
        } catch (NotFoundException e) {
            return new DeliveryCollection();
        }
    }
    public DeliveryPersonCollection getDeliveryPersons() {
        try {
            return dao.getAllDeliveryPersons();
        } catch (NotFoundException e) {
            return new DeliveryPersonCollection();
        }
    }

    public DeliveryPerson getDeliveryPerson(int id) throws DeliveryPersonNotFoundException{
        return dao.getDeliveryPersonById(id);
    }
    public Delivery getDelivery(int num) throws DeliveryNotFoundException {
        return dao.getDeliveryById(num);
    }

    public void fireDeliveryPerson(int id) throws UnfinishedDutyException, NotFoundException, CantUpdateDataException{
        DeliveryPerson temp = getDeliveryPerson(id);
        if(temp.getDeliveryCounter() > 0)
            throw new UnfinishedDutyException(id);
        try{
            dao.remove(temp);
        } catch (DeliveryNotFoundException e) {
            //ummmm.... not a bug its an easter egg
        }
    }
    public void delivered(int number) throws NotFoundException, CantUpdateDataException{
        Delivery temp = getDelivery(number);
        try {
            temp.getRef().removeDelivery();
            dao.remove(temp);
        } catch (DeliveryPersonNotFoundException e) {
            //"its not a bug its a feature" - Jesus H. Christ
        }
    }

    public void modifyDelivery(int num, String field, String newVal) throws HandsFullException, NoAvailableDeliveryPersonException, NotFoundException {
        Delivery temp = getDelivery(num);
        DeliveryPerson prev = temp.getRef();
        switch (field) {
            case "destination":
                temp.setDestination(newVal);
                break;
            case "ref":
                prev.removeDelivery();
                String lock = prev.getCity();
                prev.setCity("administrativeleave");
                assignDelivery(temp);
                prev.setCity(lock);
                break;
            default:
                throw new RuntimeException("wallahi the field is lo relevanti");

        }
    }
    public void modifyDeliveryPerson(int id, String field, String newVal) throws  UnfinishedDutyException, NotFoundException {
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
    }
}
