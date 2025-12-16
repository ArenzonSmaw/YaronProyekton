package deliveryTrio.YaronProjecton.Exceptions;

public class InvalidDeliveryInfoException extends Exception {
    public InvalidDeliveryInfoException(int id, String field, String value) {
        super("value '"+value+"' is invalid for field '"+field+"' for delivery '"+id+"'.");
    }
}
