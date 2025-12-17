package deliveryTrio.YaronProjecton.Entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPerson implements Comparable<DeliveryPerson>{
    private int delivererID=0; // when creating the object is empty, after added to the data structure id will be given
    private String name;
    private String  city;
    private int deliveryMaxCapacity; // add to properties
    private  int deliveryCounter=0;

    public DeliveryPerson(String name, String city, @Value("${deliveryPerson.maxCapacity}") int deliveryMaxCapacity){
        this.name=name;
        this.city=city;
        this.deliveryMaxCapacity=deliveryMaxCapacity;
    }
    // getters
    public int getDelivererID() {
        return delivererID;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public int getDeliveryMaxCapacity() {
        return deliveryMaxCapacity;
    }

    public int getDeliveryCounter() {
        return deliveryCounter;
    }

    // toString
    @Override
    public String toString() {
        return "DeliveryPerson{" +
                "delivererID='" + delivererID + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", deliveryMaxCapacity=" + deliveryMaxCapacity +
                ", deliveryCounter=" + deliveryCounter +
                '}';
    }
    // equals
    @Override
    public boolean equals(Object o) {
        DeliveryPerson deliveryPerson;
        if (this == o) // #1: if obj is actually this, return true, no need to continue
            return true;
        if (o == null) // #2: if obj is null, return false, dont continue to avoid null pointer exception
            return false;
        if (this.getClass() == o.getClass()) // #3: if objects types are not exactly the same, return false. No need to continue
        {
            deliveryPerson = (DeliveryPerson) o; // #4: Cast the obj into real type
            return this.delivererID == deliveryPerson.getDelivererID(); // #5: Compare the objects properties per application logic
        }
        return false;
    }
    public int compareTo(DeliveryPerson dp){
        return this.delivererID-dp.delivererID;
    }
}
