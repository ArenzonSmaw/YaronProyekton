package deliveryTrio.YaronProjecton.Exceptions.NotFound;

public class DeliveryNotFoundException extends NotFoundException {
    public DeliveryNotFoundException(int id) {
        super("No delivery with ID '" + id + "' found.");
    }
}