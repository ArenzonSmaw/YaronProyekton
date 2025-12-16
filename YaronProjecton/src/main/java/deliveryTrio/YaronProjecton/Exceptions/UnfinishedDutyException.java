package deliveryTrio.YaronProjecton.Exceptions;

public class UnfinishedDutyException extends Exception {
    public UnfinishedDutyException(int deliverymanID) {
        super("cannot modify delivery person " + deliverymanID + " because he has packages he has not yet delivered.");
    }
}
