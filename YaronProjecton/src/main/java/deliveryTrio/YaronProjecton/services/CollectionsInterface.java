package deliveryTrio.YaronProjecton.services;


import java.util.ArrayList;

public interface CollectionsInterface<T> {
    public boolean add(T t);
    public boolean remove(T t);
    public  T getObjectById(int id);
    public boolean updateObject(T t);
    public ArrayList<T> getList();
    public int amount();
}
