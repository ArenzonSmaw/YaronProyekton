package deliveryTrio.YaronProjecton.exceptions.invalidInfo;

public class InvalidDeliveryInfoException extends InvalidInfoException {
    public InvalidDeliveryInfoException(int id, String field, String value) {
        super("Value '" + value + "' is invalid for field '" + field + "' for delivery '" + id + "'.");
    }
}
