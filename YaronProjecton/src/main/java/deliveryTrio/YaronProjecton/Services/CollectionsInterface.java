package deliveryTrio.YaronProjecton.Services;


import java.util.ArrayList;
import java.util.List;

public interface CollectionsInterface<T> {
    public boolean add(T t);
    public boolean remove(T t);
    public  T getObjectById(int id);
    public boolean updateObject(T t);
    public ArrayList<T> getList();
    public int amount();
}
