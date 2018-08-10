package dataAccessLayer;

import entity.Node;
import entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDA {
    private Connection connection;

    public ProductDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    public List<Product> getAllProduct() throws SQLException {
//        Product product = null;
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product.productID,product.quantity , product.exportPrice, product.exportPrice,producttype.typeID,unit.unitID,provider.providerID,product.name,product.shortName,CONCAT(provider.shortName,LPAD(producttype.typeID,3 ,'0'),product.shortName,LPAD(product.productID,5 ,'0')),product.imageLink,producttype.name,provider.name,unit.name,product.description\n" +
                "\t\tFROM product , producttype, unit, provider,`type-product`,`provider-product`,`importticket-product-provider`,importticket\n" +
                "\t\tWHERE  producttype.typeID = `type-product`.typeID and  product.productID = `type-product`.`productID`\n" +
                "\t\tand provider.providerID= `provider-product`.`providerID` and `provider-product`.`productID` = product.productID and `type-product`.`unitID` = unit.unitID and `importticket-product-provider`.`productID` = `provider-product`.`productID` and `importticket-product-provider`.`providerID` =  `provider-product`.`providerID`and `importticket-product-provider`.`importTicketID` = importticket.importTicketID and importticket.date < CURRENT_DATE ORDER by product.productID;";
        Statement statement = connection.createStatement();
//            statement.executeUpdate();
        ResultSet resultSet = statement.executeQuery(sql);

        int productID;
        int quantity;
        int impPrice;
        int expPrice;
        int typeID;
        int unitID;
        int providerID;
        String name;
        String shortName;
        String productCode;
        String imageLink;
        String type;
        String providerName;
        String unit;
        String description;

        while (resultSet.next()) {
            productID = resultSet.getInt(1);
            quantity = resultSet.getInt(2);
            impPrice = resultSet.getInt(3);
            expPrice = resultSet.getInt(4);
            typeID = resultSet.getInt(5);
            unitID = resultSet.getInt(6);
            providerID = resultSet.getInt(7);
            name = resultSet.getString(8);
            shortName = resultSet.getString(9);
            productCode = resultSet.getString(10);
            imageLink = resultSet.getString(11);
            type = resultSet.getString(12);
            providerName = resultSet.getString(13);
            unit = resultSet.getString(14);
            description = resultSet.getString(15);
            Product product = new Product(productID, quantity, impPrice, expPrice, typeID, unitID, providerID, name, shortName, productCode, imageLink, type, providerName, unit, description);
            list.add(product);
        }
        statement.close();
        return list;
    }

    public List<Product> getQuotation(int providerID) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product.name, producttype.name, `provider-product`.`price`\n" +
                "from product , producttype ,`provider-product`,`type-product`\n" +
                "WHERE `provider-product`.`productID` = product.productID and`type-product`.`productID` = product.productID and `type-product`.`typeID` = producttype.typeID and `provider-product`.`providerID` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, providerID);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Product product = new Product(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3));
            list.add(product);
        }
        statement.close();
        return list;
    }

    public List<Node> getAllType() throws SQLException {
        List<Node> list = new ArrayList<>();
        String sql = "SELECT typeID , name FROM `producttype`;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Node node = new Node(resultSet.getInt(1), resultSet.getString(2));
            list.add(node);
        }
        statement.close();
        return list;
    }

    public List<Node> getAllUnit() throws SQLException {
        List<Node> list = new ArrayList<>();
        String sql = "SELECT * FROM `unit`;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Node node = new Node(resultSet.getInt(1), resultSet.getString(2));
            list.add(node);
        }
        statement.close();
        return list;
    }

    public boolean checkDuplicatedShortName(String shortName) throws SQLException {
        String sql = "SELECT `productID` FROM `product` WHERE `shortName` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, shortName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public int addProduct(Product product, String createdBy) throws SQLException {
        String sql = "INSERT INTO `product` (`productID`, `name`, `shortName`, `quantity`, `exportPrice`, `imageLink`, `description`, `deleted`, `createdBy`, `createDate`, `modifiedBy`, `modifiedDate`) " +
                "VALUES (NULL, ?, ?, ?, NULL,?, ?, '0', ?, CURRENT_TIMESTAMP, NULL, NULL);";

        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, product.getName());
        statement.setString(2, product.getShortName());
        statement.setInt(3, product.getQuantity());
        statement.setString(4, product.getImageLink());
        statement.setString(5, product.getDescription());
        statement.setString(6, createdBy);

        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();

        int generatedKey = 0;
        if (resultSet.next()) {
            generatedKey = resultSet.getInt(1);
        }

        addUnitAndType(product.getTypeID(), generatedKey, product.getUnitID());

        addProviderAndProduct(product.getProviderID(), generatedKey, product.getImpPrice());

        statement.close();
        return generatedKey;
    }

    private void addUnitAndType(int typeID, int productID, int unitID) throws SQLException {
        String sql = "INSERT INTO `type-product` (`typeID`, `productID`, `unitID`) VALUES (?,?,?);";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, typeID);
        statement.setInt(2, productID);
        statement.setInt(3, unitID);

        statement.executeUpdate();
        statement.close();
    }

    private void addProviderAndProduct(int providerID, int productID, int price) throws SQLException {
        String sql = "INSERT INTO `provider-product` (`providerID`, `productID`, `price`) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, providerID);
        statement.setInt(2, productID);
        statement.setInt(3, price);

        statement.executeUpdate();
        statement.close();
    }


}
