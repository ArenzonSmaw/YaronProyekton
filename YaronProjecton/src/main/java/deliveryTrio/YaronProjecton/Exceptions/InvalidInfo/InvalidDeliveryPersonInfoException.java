package deliveryTrio.YaronProjecton.Exceptions;

public class InvalidDeliveryPersonInfoException extends Exception {
    public InvalidDeliveryPersonInfoException(int id, String field, String value) {
        super("value '"+value+"' is invalid for field '"+field+"' for delivery person '"+id+"'.");
    }
}
