package entity;

public class Salary {
    private int id;
    private String name, description;
    private int basic;

    public Salary(int id, String name, String description, int basic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basic = basic;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return this.name + " - " + this.basic;
    }
}
