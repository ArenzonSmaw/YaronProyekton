package deliveryTrio.YaronProjecton.Exceptions;

public class DeliveryNotFoundException extends NotFoundException {
    public DeliveryNotFoundException(int id) {
        super("No delivery with id '"+id+"' found.");
    }
}