package deliveryTrio.YaronProjecton.Exceptions.NotFound;

public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String obj, String primeKey) {
        super("No "+obj+" with primary key '"+primeKey+"' found.");
    }
}
