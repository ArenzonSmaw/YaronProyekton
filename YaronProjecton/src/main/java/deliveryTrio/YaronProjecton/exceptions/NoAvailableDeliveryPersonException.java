package deliveryTrio.YaronProjecton.exceptions;

public class NoAvailableDeliveryPersonException extends Exception {
    public NoAvailableDeliveryPersonException(int deliveryNo, String city) {
        super("No available delivery men in '" + city + "' to deliver delivery: '" + deliveryNo + "'.");
    }
}
