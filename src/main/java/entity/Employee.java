package entity;

import java.util.Date;

public class Employee extends Human {
    private int basicSalary , bonus, punish;
    private Date dateOfBirth, dateOfbegin , dateOfEnd;
    private String  imageLink,username , password,phoneNumber,facebook;
    private boolean isAdmin ;

    public Employee(int id, String name, String gender, String description, String address, String phoneNumber,
                    String facebook, int basicSalary, int bonus, int punish, Date dateOfBirth, Date dateOfbegin,
                    Date dateOfEnd, String imageLink, String username, String password, boolean isAdmin) {
        super(id, name, gender, description, address, phoneNumber, facebook);
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.punish = punish;
        this.dateOfBirth = dateOfBirth;
        this.dateOfbegin = dateOfbegin;
        this.dateOfEnd = dateOfEnd;
        this.imageLink = imageLink;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.phoneNumber = phoneNumber;
        this.facebook = facebook;
    }

    public Employee(int id, String username, String password, boolean isAdmin) {
        super(id);
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name=" + name +
                ", gender=" + gender +
                ", description=" + description +
                ", basicSalary=" + basicSalary +
                ", bonus=" + bonus +
                ", punish=" + punish +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfbegin=" + dateOfbegin +
                ", dateOfEnd=" + dateOfEnd +
                ", imageLink='" + imageLink + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", facebook='" + facebook + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getBasicSalary() {
        return basicSalary;
    }

    public int getBonus() {
        return bonus;
    }

    public int getPunish() {
        return punish;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateOfbegin() {
        return dateOfbegin;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFacebook() {
        return facebook;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }
}
