package entity;

public class Product {
    private int id, quantity,  price;
    private String name, unit, image, description, provider, type;

    public Product() {
    }

    public Product(int id, int quantity, int price, String name, String unit,
                   String image, String description, String provider, String type) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.unit = unit;
        this.image = image;
        this.description = description;
        this.provider = provider;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getProvider() {
        return provider;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
