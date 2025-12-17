package deliveryTrio.YaronProjecton.Services.DataAccess;

import deliveryTrio.YaronProjecton.Exceptions.NotFound.DeliveryPersonNotFoundException;

public interface IDAO {
    boolean add(Object obj);
    boolean remove(Object obj) throws DeliveryPersonNotFoundException;
    boolean modify(Object updated);
    boolean wakeup();
}
