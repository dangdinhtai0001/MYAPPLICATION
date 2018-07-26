package entity;

public class Salary {
    private String name, description ;
    private int basic ;

    public Salary(String name, String description, int basic) {
        this.name = name;
        this.description = description;
        this.basic = basic;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBasic() {
        return basic;
    }
}
