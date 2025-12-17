package deliveryTrio.YaronProjecton.Services.DeliveryPerson;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DeliveryPersonCollection implements deliveryTrio.YaronProjecton.Services.CollectionsInterface<DeliveryPerson>,Iterable<DeliveryPerson>{
    private List<DeliveryPerson> deliveryPersons;
    private int lastID;

    public DeliveryPersonCollection(){
        deliveryPersons=new ArrayList<DeliveryPerson>();
    }

    @PostConstruct
    public void initialize(@Value("${deliveryPersonCollection.startingID}") int stID) {
        lastID = stID;
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

    public void showDeliveryPersons(){
        System.out.println("The Ones Who Deliver:");
        for (DeliveryPerson dp : deliveryPersons)
            System.out.println(dp);
    }
}
