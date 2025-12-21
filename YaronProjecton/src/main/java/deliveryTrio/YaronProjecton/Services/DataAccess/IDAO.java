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

import java.util.HashMap;

public interface IDAO {
    // adds new object to the app
    boolean add(Object obj) throws CantUpdateDataException;
    // remove an object from the app
    boolean remove(Object obj) throws DeliveryPersonNotFoundException, DeliveryNotFoundException, CantUpdateDataException;
    // modifies an exist object
    boolean modify(Object updated) throws DeliveryPersonNotFoundException,DeliveryNotFoundException,CantUpdateDataException;
    // return an array list of all the delivery persons
    DeliveryPersonCollection getAllDeliveryPersons() throws NotFoundException;
    // return an array list of all the deliveries
    DeliveryCollection getAllDeliveries() throws NotFoundException;
    // return a delivery persons by its id
    DeliveryPerson getDeliveryPersonById(int id)  throws DeliveryPersonNotFoundException;
    // return a delivery by its id
    Delivery getDeliveryById(int id) throws DeliveryNotFoundException;
    // return an hashmap with important data about the app
    HashMap<String,Integer> appData();
    // loads the app data
    boolean wakeup() throws CantAccessDataException;
    // updates the app data
    boolean update() throws CantAccessDataException;
}
