package entity;

public class Human {
    protected int id ;
    protected String name , gender , description ,address , phoneNumber , facebook;

    public Human(int id, String name, String gender, String description, String address, String phoneNumber, String facebook) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.facebook = facebook;
    }

    public Human(int id) {
        this.id = id;
    }
}
