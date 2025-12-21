package deliveryTrio.YaronProjecton.Entities;


import deliveryTrio.YaronProjecton.Exceptions.HandsFullException;
import deliveryTrio.YaronProjecton.Exceptions.NotFound.NotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class DeliveryPerson implements Comparable<DeliveryPerson>,Serializable{
    private int delivererID; // when creating the object is empty, after added to the data structure id will be given
    @NotNull
    private String name;
    @NotNull
    @Min(value=3)
    private String  city;
    private int deliveryMaxCapacity; // add to properties
    private int deliveryCounter = 0;
    // static attributes
    public static int defaultMaxCapacity;
    private static final long serialVersionUID = 1L;

    public DeliveryPerson(String name, String city, int workerID){
        this.name=name;
        this.city=city;
        delivererID = workerID;
        this.deliveryMaxCapacity = defaultMaxCapacity;
    }
    // getters
    public int getDelivererID() {
        return delivererID;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String nCity) {
        city = nCity;
    }

    public void setMaxCapacity(String nMax) {
        int max = 0;
        for (int i = 0; i < nMax.length(); i++) {
            max *= 10;
            max += nMax.charAt(i) - '0';
        }
        deliveryMaxCapacity = max;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public int getDeliveryMaxCapacity() {
        return deliveryMaxCapacity;
    }

    public int getDeliveryCounter() {
        return deliveryCounter;
    }

    public int getID() {return delivererID;}

    public void addDelivery() throws HandsFullException{
        if (deliveryCounter == deliveryMaxCapacity) {
            throw new HandsFullException(delivererID);
        } else{
            deliveryCounter++;
        }
    }
    public void removeDelivery() throws NotFoundException{
        if (deliveryCounter == 0) {
            throw new NotFoundException("Delivery person '" + delivererID + "' has no deliveries");
        } else{
            deliveryCounter--;
        }
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
