package deliveryTrio.YaronProjecton.Services.DeliveryPerson;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Services.CollectionsInterface;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class DeliveryPersonCollection implements CollectionsInterface<DeliveryPerson>,Iterable<DeliveryPerson>, Serializable {
    private ArrayList<DeliveryPerson> deliveryPersons;
//    @Value("${deliveryPersonCollection.startingID}")
//    private int lastID;
    // static attributes
    private static final long serialVersionUID = 3L;
    public DeliveryPersonCollection(){
        deliveryPersons=new ArrayList<DeliveryPerson>();
    }

    @Override
    public Iterator<DeliveryPerson> iterator() {
        return new DeliveryPersonIterator(deliveryPersons);
    }
    @Override
    public boolean add(DeliveryPerson deliveryPerson) {
        return deliveryPersons.add(deliveryPerson);
    }
    @Override
    public boolean remove(DeliveryPerson deliveryPerson) {
        return deliveryPersons.remove(deliveryPerson);
    }
    @Override
    public DeliveryPerson getObjectById(int id) {
        for(DeliveryPerson dp : deliveryPersons){
            if (dp.getDelivererID()==id)
                return dp;
        }
        return null;
    }
    @Override
    public boolean updateObject(DeliveryPerson deliveryPerson) {
        for (int i = 0; i < deliveryPersons.size(); i++) {
            if (deliveryPersons.get(i).equals(deliveryPerson)) {
                deliveryPersons.set(i, deliveryPerson);
                return true;
            }
        }
        return false;
    }
    @Override
    public ArrayList<DeliveryPerson> getList() {
        return deliveryPersons;
    }
    @Override
    public int amount() {
        return deliveryPersons.size();
    }

    public void showDeliveryPersons(){
        System.out.println("The Ones Who Deliver:");
        for (DeliveryPerson dp : deliveryPersons)
            System.out.println(dp);
    }
}
