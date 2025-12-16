package deliveryTrio.YaronProjecton.Exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String primaryKey) {
        super("No object with primary key '"+primaryKey+"' found.");
    }
}
