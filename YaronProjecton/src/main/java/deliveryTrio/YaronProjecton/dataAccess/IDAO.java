package deliveryTrio.YaronProjecton.dataAccess;
import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.dataSavingProblems.CantAccessDataException;
import deliveryTrio.YaronProjecton.exceptions.dataSavingProblems.CantUpdateDataException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.dataAccess.delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.dataAccess.deliveryPerson.DeliveryPersonCollection;

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
