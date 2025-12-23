package deliveryTrio.YaronProjecton.exceptions.notFound;

public class DeliveryPersonNotFoundException extends NotFoundException {
    public DeliveryPersonNotFoundException(int id) {
        super("No delivery person with ID '" + id + "' found.");
    }
}
