package deliveryTrio.YaronProjecton.Exceptions.InvalidInfo;

public class InvalidDeliveryPersonInfoException extends InvalidInfoException {
    public InvalidDeliveryPersonInfoException(int id, String field, String value) {
        super("value '"+value+"' is invalid for field '"+field+"' for delivery person '"+id+"'.");
    }
}
