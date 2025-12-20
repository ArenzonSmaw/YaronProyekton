package deliveryTrio.YaronProjecton.Exceptions;

public class TooManyDeliveryPersonsException extends Exception{
    public TooManyDeliveryPersonsException(int limit) {
        super("Our company will get bankrupt! We have reached to limit of the delivery persons "+limit+ ", Bad Boy!");
    }
}
