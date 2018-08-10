package entity;

public class Product {
    private int productID, quantity, impPrice, expPrice, typeID, unitID, providerID;
    private String name, shortName, productCode, imageLink, type, providerName, unit, description;

    public Product(int productID, int quantity, int impPrice, int expPrice, int typeID, int unitID, int providerID,
                   String name, String shortName, String productCode, String imageLink, String type,
                   String providerName, String unit, String description) {
        this.productID = productID;
        this.quantity = quantity;
        this.impPrice = impPrice;
        this.expPrice = expPrice;
        this.typeID = typeID;
        this.unitID = unitID;
        this.providerID = providerID;
        this.name = name;
        this.shortName = shortName;
        this.productCode = productCode;
        this.imageLink = imageLink;
        this.type = type;
        this.providerName = providerName;
        this.unit = unit;
        this.description = description;
    }

    public Product(String name, String type, int impPrice) {
        this.impPrice = impPrice;
        this.name = name;
        this.type = type;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getImpPrice() {
        return impPrice;
    }

    public int getExpPrice() {
        return expPrice;
    }

    public int getTypeID() {
        return typeID;
    }

    public int getUnitID() {
        return unitID;
    }

    public String getName() {
        return name;
    }

    public int getProviderID() {
        return providerID;
    }

    public String getShortName() {
        return shortName;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public String getProviderName() {
        return providerName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", quantity=" + quantity +
                ", impPrice=" + impPrice +
                ", expPrice=" + expPrice +
                ", typeID=" + typeID +
                ", unitID=" + unitID +
                ", providerID=" + providerID +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", type='" + type + '\'' +
                ", providerName='" + providerName + '\'' +
                '}';
    }
}
