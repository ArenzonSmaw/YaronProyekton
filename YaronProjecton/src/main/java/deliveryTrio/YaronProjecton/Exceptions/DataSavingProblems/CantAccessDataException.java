package deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems;

public class CantAccessDataException extends Exception{
    public CantAccessDataException(){
        super("Can't access the app's data");
    }
}
