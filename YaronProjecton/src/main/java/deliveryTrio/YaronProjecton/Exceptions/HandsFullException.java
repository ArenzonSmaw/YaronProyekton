package deliveryTrio.YaronProjecton.Exceptions;

public class HandsFullException extends Exception {
    public HandsFullException(int id) {
        super("Delivery person '" + id + "''s hands are full. cut him some slack.");
    }
}
