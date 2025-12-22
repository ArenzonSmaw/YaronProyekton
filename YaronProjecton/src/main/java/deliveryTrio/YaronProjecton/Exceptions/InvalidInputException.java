package deliveryTrio.YaronProjecton.Exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String input, String msg) {
        super("The input: '" + input + "' is not a valid input. Please try again.\n" + msg);
    }
}
