package deliveryTrio.YaronProjecton.Exceptions.DataSavingProblems;

public class CantUpdateDataException extends Exception{
    public CantUpdateDataException(){
        super("The system cant update the app data, please restart the app");
    }
}
