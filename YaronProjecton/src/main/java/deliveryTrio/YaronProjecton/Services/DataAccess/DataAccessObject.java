package deliveryTrio.YaronProjecton.Services.DataAccess;
import deliveryTrio.YaronProjecton.Entities.Delivery;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems.CantAccessDataException;
import deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems.CantUpdateDataException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.NotFoundException;
import deliveryTrio.YaronProjecton.Services.Delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonCollection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

@Component
public class DataAccessObject implements IDAO{
    // the collections classes attributes
    private DeliveryPersonCollection deliveryPersons;
    private DeliveryCollection deliveries;

    @PostConstruct
    public void init() {
        // auto init of the data
        System.out.println("----------------------------------------------");
        try {
            wakeup();
            System.out.println("            Loading App Data");
        } catch (CantAccessDataException e) {
            // creating these for so wont be null if the data isn't availble
            System.out.println("Can't access the data files, creating new empty ones");
            deliveryPersons = new DeliveryPersonCollection();
            deliveries = new DeliveryCollection();
        }
        finally {
            System.out.println("----------------------------------------------");
        }
    }
    public DataAccessObject(){}
    @PreDestroy
    public void ensureSave() {
        System.out.println("----------------------------------------------");
        // saves the last changes data
        try {
            update();
            System.out.println("                Auto save");
        } catch (CantAccessDataException e) {
            System.err.println("        Failed to save the last changes");
        }
        finally {
            System.out.println("----------------------------------------------");
        }
    }

    @Override
    public boolean add(Object obj) throws CantUpdateDataException {
        // adds an object to the system
        try {
            if(obj.getClass() == DeliveryPerson.class){
                if(deliveryPersons.add((DeliveryPerson) obj))
                    return update(); // updates the data
                return false;
            }
            else if (obj.getClass() == Delivery.class){
                if(deliveries.add((Delivery) obj))
                    return update();
                return false;
            }
            else {
                return false; //throw invalid obj except
            }
        }
        catch (CantAccessDataException e){
            throw new CantUpdateDataException();
        }

    }
    @Override
    public boolean remove(Object obj) throws DeliveryPersonNotFoundException,DeliveryNotFoundException,CantUpdateDataException {
        // removes delivery person or delivery from the app data backup
        try {
            if(obj.getClass() == DeliveryPerson.class) {
                if(deliveryPersons.remove((DeliveryPerson) obj)){
                    return update();
                }
                throw new DeliveryPersonNotFoundException(((DeliveryPerson)obj).getDelivererID());
            }
            else if(obj.getClass() == Delivery.class){
                if(deliveries.remove((Delivery) obj)){
                    return update();
                }
                throw new DeliveryNotFoundException(((Delivery)obj).getDeliveryNo());
            }
            else{
                return false; // unfimiliar object
            }
        }
        catch (CantAccessDataException e){
            throw new CantUpdateDataException();
        }
    }
    @Override
    public boolean modify(Object updated)throws DeliveryPersonNotFoundException,DeliveryNotFoundException,CantUpdateDataException {
        // adds an object to the system
        try {
            if(updated.getClass() == DeliveryPerson.class){
                DeliveryPerson dp=(DeliveryPerson) updated;
                if(deliveryPersons.updateObject(dp))
                    return update(); // updates the data
                throw new DeliveryPersonNotFoundException(dp.getDelivererID());
            }
            else if (updated.getClass() == Delivery.class){
                Delivery dl=(Delivery) updated;
                if(deliveries.updateObject(dl))
                    return update();
                throw new DeliveryNotFoundException(dl.getDeliveryNo());
            }
            else {
                return false; //throw invalid obj except
            }
        }
        catch (CantAccessDataException e){
            throw new CantUpdateDataException();
        }

    }
    @Override
    public DeliveryPersonCollection getAllDeliveryPersons() throws NotFoundException {
        if (deliveryPersons.getList().isEmpty()) {
            throw new NotFoundException("There are no delivery persons yet");
        }
        else{
            return deliveryPersons;
        }
    }
    @Override
    public DeliveryCollection getAllDeliveries() throws NotFoundException {
        if (deliveries.getList().isEmpty()){
            throw new NotFoundException("There are no deliveries yet");
        }
        else{
            return deliveries;
        }
    }
    @Override
    public DeliveryPerson getDeliveryPersonById(int id) throws DeliveryPersonNotFoundException {
         DeliveryPerson dlp=deliveryPersons.getObjectById(id);
         if (dlp==null)
             throw new DeliveryPersonNotFoundException(id);
         return dlp;
    }
    @Override
    public Delivery getDeliveryById(int id) throws DeliveryNotFoundException {
        Delivery dl=deliveries.getObjectById(id);
        if (dl==null)
            throw new DeliveryNotFoundException(id);
        return dl;
    }
    @Override
    public HashMap<String, Integer> appData() {
        // returns a dict with data about the app
        HashMap<String,Integer> dataMap= new HashMap<String,Integer>();
        dataMap.put("DPA",deliveryPersons.amount());
        dataMap.put("DLA",deliveries.amount());
        return dataMap;
    }

    @Override
    public boolean wakeup() throws CantAccessDataException {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        try {
            // gets the deliveries collection
            fileInputStream= new FileInputStream("deliveries_data.txt");
            objectInputStream=new ObjectInputStream(fileInputStream);
            deliveries  = (DeliveryCollection) objectInputStream.readObject();
            objectInputStream.close();
            // gets the delivery persons collection
            fileInputStream= new FileInputStream("delivery_persons_data.txt");
            objectInputStream= new ObjectInputStream(fileInputStream);
            deliveryPersons  = (DeliveryPersonCollection) objectInputStream.readObject();
            objectInputStream.close();
            return true;
        }
        catch (Exception e){
            throw new CantAccessDataException();
        }
    }
    @Override
    public boolean update() throws CantAccessDataException {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        try {
            fileOutputStream= new FileOutputStream("deliveries_data.txt");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(deliveries);
            objectOutputStream.flush();
            objectOutputStream.close();

            fileOutputStream= new FileOutputStream("delivery_persons_data.txt");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(deliveryPersons);
            objectOutputStream.flush();
            objectOutputStream.close();

            return true;
        }
        catch (Exception e){
            throw new CantAccessDataException();
        }
    }
}
