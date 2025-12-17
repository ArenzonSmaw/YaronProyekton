package deliveryTrio.YaronProjecton.Services.DataAccess;


import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonCollection;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonIterator;

import javax.xml.crypto.Data;
import java.rmi.NoSuchObjectException;


public class DataAccessObject implements IDAO{
    private DeliveryPersonCollection delPerson;
    //private DeliveryCollection deliveryIt;

    public DataAccessObject() {
    }
    @Override
    public boolean add(Object obj) {
        if(obj.getClass() == DeliveryPerson.class) {
            delPerson.add((DeliveryPerson) obj);
            return true;
        }
        //else if (obj.getClass() == Delivery.class)
        else {
            return false; //throw invalid obj except
        }
    }

    @Override
    public boolean remove(Object obj) throws DeliveryPersonNotFoundException {
        if(obj.getClass() == DeliveryPerson.class) {
            DeliveryPerson dp = (DeliveryPerson) obj;
            if(delPerson.remove(dp))
                return true;
            else
                throw new DeliveryPersonNotFoundException(dp.getDelivererID());
        }
        return false;
    }

    @Override
    public boolean modify(Object updated) {
        return false;
    }

    @Override
    public boolean wakeup() {
        return false;
    }
}
