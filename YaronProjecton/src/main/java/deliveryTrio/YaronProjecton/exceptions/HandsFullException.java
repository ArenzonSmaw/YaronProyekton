package deliveryTrio.YaronProjecton.exceptions;

public class HandsFullException extends Exception {
    public HandsFullException(int id) {
        super("delivery person '" + id + "''s hands are full. cut him some slack.");
    }
}
