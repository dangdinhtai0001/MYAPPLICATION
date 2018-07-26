package entity;

import java.util.ArrayList;

public class Provider {
    private int providerID ;
    private String name , address, contact ,description;
    private ArrayList<Integer> productID ;

    public Provider(int providerID, String name, String address, String contact, String description,
                    ArrayList<Integer> productID) {
        this.providerID = providerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.description = description;
        this.productID = productID;
    }

    public Provider(int providerID, String name, String address, String contact, ArrayList<Integer> productID) {
        this.providerID = providerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.productID = productID;
    }

    public Provider(int providerID, String name, String address, String contact) {
        this.providerID = providerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public Provider(int providerID, String name, String address, String contact, String description) {
        this.providerID = providerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.description = description;
    }

    public Provider() {
    }

    @Override
    public String toString() {
        return "Provider{" +
                "providerID=" + providerID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", description='" + description + '\'' +
                ", productID=" + productID +
                '}';
    }

    public int getProviderID() {
        return providerID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Integer> getProductID() {
        return productID;
    }
}
