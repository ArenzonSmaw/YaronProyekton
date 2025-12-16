package deliveryTrio.YaronProjecton.Exceptions.InvalidInfo;

public class InvalidDeliveryInfoException extends InvalidInfoException {
    public InvalidDeliveryInfoException(int id, String field, String value) {
        super("value '"+value+"' is invalid for field '"+field+"' for delivery '"+id+"'.");
    }
}
