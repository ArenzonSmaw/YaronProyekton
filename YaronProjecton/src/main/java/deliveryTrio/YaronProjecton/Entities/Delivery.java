package deliveryTrio.YaronProjecton.Entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class Delivery implements Comparable<Delivery>, Serializable {
    private int deliveryNo;
    @NotNull(message = "Delivery must be weighed")
    @Min(value=0)
    private double weight;
    @NotBlank(message = "Delivery must have a destination")
    @Size(min=3, message = "Destination must be at least 3 characters")
    private String destination;
    @NotNull(message = "Delivery must have a customer")
    private String customerID;
    private DeliveryPerson ref;
    // static attributes
    private static final long serialVersionUID = 2L;

    public Delivery(double weight, String destination, String customerID, int deliveryID){
        //Doesn't receive a delivery ID or reference to DeliveryPerson, will receive when added to the system by the delivery manager
        this.weight = weight;
        this.destination = destination;
        this.customerID = customerID;
        deliveryNo = deliveryID;
    }

    public int getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(int deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public DeliveryPerson getRef() {
        return ref;
    }

    public void setRef(DeliveryPerson ref) {
        this.ref = ref;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getID() {return deliveryNo;}

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryNo=" + deliveryNo +
                ", weight=" + weight +
                ", destination='" + destination + '\'' +
                ", customerID='" + customerID + '\'' +
                ", ref=" + ref +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        Delivery s;
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() == o.getClass())
        {
            s = (Delivery) o;
            if (this.deliveryNo == 0){
                return customerID.equals(s.customerID);
            }

            return deliveryNo == s.deliveryNo;
        }
        return false;
    }
    public int compareTo(Delivery d){
        if (this.deliveryNo == 0){
            return this.customerID.compareTo(d.customerID);
        }

        return this.deliveryNo - d.deliveryNo;
    }

}

