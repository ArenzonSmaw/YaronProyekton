package deliveryTrio.YaronProjecton.Services.Delivery;
import deliveryTrio.YaronProjecton.Entities.Delivery;
import deliveryTrio.YaronProjecton.Services.CollectionsInterface;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class DeliveryCollection implements CollectionsInterface<Delivery>, Iterable<Delivery>, Serializable {
    private ArrayList<Delivery> deliveries;
//    @Value("${deliveriesCollection.startingID}")
//    private int lastID;
    // static attributes
    private static final long serialVersionUID = 4L;


    public DeliveryCollection(){
        deliveries=new ArrayList<Delivery>();
    }
    @Override
    public boolean add(Delivery d){
        return deliveries.add(d);
    }
    @Override
    public boolean remove(Delivery d){
        return deliveries.remove(d);
    }
    @Override
    public Delivery getObjectById(int ID){
        for (int i = 0; i < deliveries.size(); i++) {
            if (deliveries.get(i).getDeliveryNo() == ID){
                return deliveries.get(i);
            }
        }
        return null;
    }
    @Override
    public boolean updateObject(Delivery delivery){
        for (int i = 0; i < deliveries.size(); i++){
            if (delivery.equals(deliveries.get(i))){
                deliveries.set(i, delivery);
                return true;
            }
        }
        return false;
    }
    @Override
    public ArrayList<Delivery> getList() {
        return deliveries;
    }
    @Override
    public int amount() {
        return deliveries.size();
    }
    @Override
    public Iterator<Delivery> iterator() {
        return new DeliveryIterator(deliveries);
    }
}