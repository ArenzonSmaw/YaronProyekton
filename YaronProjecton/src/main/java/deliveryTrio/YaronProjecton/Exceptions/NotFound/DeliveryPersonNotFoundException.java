package deliveryTrio.YaronProjecton.Exceptions.NotFound;

public class DeliveryPersonNotFoundException extends NotFoundException {
    public DeliveryPersonNotFoundException(int id) {
        super("No delivery person with id '"+id+"' found.");
    }
}
