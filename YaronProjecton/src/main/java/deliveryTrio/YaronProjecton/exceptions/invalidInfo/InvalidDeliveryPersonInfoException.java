package deliveryTrio.YaronProjecton.exceptions.invalidInfo;

public class InvalidDeliveryPersonInfoException extends InvalidInfoException {
    public InvalidDeliveryPersonInfoException(int id, String field, String value) {
        super("value '"+value+"' is invalid for field '"+field+"' for delivery person '"+id+"'.");
    }
}
