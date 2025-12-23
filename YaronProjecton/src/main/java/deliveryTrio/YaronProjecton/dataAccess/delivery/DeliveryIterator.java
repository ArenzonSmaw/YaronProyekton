package deliveryTrio.YaronProjecton.dataAccess.delivery;

import deliveryTrio.YaronProjecton.entities.Delivery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class DeliveryIterator implements Iterator<Delivery> {
    private ArrayList<Delivery> arr;
    private int position;

    public DeliveryIterator(ArrayList<Delivery> arr){
        this.arr = arr;
    }

    @Override
    public boolean hasNext() {
        return position < arr.size() && arr.get(position) != null;
    }

    @Override
    public Delivery next() {
        if (!hasNext()){
            //TODO: throw exception
        }
        return arr.get(position++);
    }
}

