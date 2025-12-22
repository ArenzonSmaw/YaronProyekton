package deliveryTrio.YaronProjecton.Exceptions;

public class UnfinishedDutyException extends Exception {
    public UnfinishedDutyException(int deliverymanID) {
        super("Cannot modify / fire delivery person '" + deliverymanID + "' because he has packages he has not yet delivered.");
    }
}
