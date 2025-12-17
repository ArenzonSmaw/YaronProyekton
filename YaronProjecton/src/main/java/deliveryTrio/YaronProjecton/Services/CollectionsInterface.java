package deliveryTrio.YaronProjecton.Services;

public interface CollectionsInterface<T> {
    public boolean add(T t);
    public boolean remove(T t);
    public  T getObjectById(int id);
    public boolean updateObject(T t);
}
