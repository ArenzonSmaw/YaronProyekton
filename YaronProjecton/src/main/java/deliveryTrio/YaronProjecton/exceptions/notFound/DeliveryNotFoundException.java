package deliveryTrio.YaronProjecton.exceptions.notFound;

public class DeliveryNotFoundException extends NotFoundException {
    public DeliveryNotFoundException(int id) {
        super("No delivery with ID '" + id + "' found.");
    }
}