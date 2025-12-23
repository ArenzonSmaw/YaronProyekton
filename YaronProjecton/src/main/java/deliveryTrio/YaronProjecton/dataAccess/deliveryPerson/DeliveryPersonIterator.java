package deliveryTrio.YaronProjecton.dataAccess.deliveryPerson;

import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service

public class DeliveryPersonIterator implements Iterator<DeliveryPerson> {
    private  List<DeliveryPerson> deliveryPersons;
    private int pos;
    public DeliveryPersonIterator( List<DeliveryPerson> deliveryPersons){
        this.deliveryPersons=deliveryPersons;
    }
    @Override
    public boolean hasNext() {
        return pos<deliveryPersons.size();
    }
    @Override
    public DeliveryPerson next() {
        if(!hasNext())
            throw new NoSuchElementException(String.valueOf(pos));
        return deliveryPersons.get(pos++);

    }
}
