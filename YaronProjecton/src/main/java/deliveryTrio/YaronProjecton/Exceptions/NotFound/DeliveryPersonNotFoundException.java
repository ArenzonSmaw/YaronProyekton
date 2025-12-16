package deliveryTrio.YaronProjecton.Exceptions;

public class DeliveryPersonNotFoundException extends Exception {
    public DeliveryPersonNotFoundException(int id) {
        super("No delivery person with id '"+id+"' found.");
    }
}
