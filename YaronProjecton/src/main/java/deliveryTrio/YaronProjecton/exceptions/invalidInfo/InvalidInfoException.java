package deliveryTrio.YaronProjecton.exceptions.invalidInfo;

public class InvalidInfoException extends Exception {
    public InvalidInfoException(String primeKey, String field, String value) {
        super("Invalid value '"+value+"' for field '"+field+"' for object with primary key '"+primeKey+"'.");
    }
    public InvalidInfoException(String message) {
        super(message);
    }
}
