package deliveryTrio.YaronProjecton.exceptions.dataSavingProblems;

public class CantAccessDataException extends Exception{
    public CantAccessDataException(){
        super("Can't access the app's data");
    }
}
