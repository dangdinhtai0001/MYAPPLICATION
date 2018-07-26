package dataAccessLayer;

import entity.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDA {
    private Connection connection ;

    public ProductDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    public Product getAProduct(int productID , int providerID) throws SQLException {
        Product product = null;
        String sql = "SELECT  product.name, provider.name,producttype.name , product.quantity ,unit.name  ," +
                " product.exportPrice , product.imageLink, product.description FROM product , `type-product`, " +
                "producttype, unit,provider , `provider-product` WHERE product.productID = `type-product`.`productID` " +
                "AND producttype.typeID = `type-product`.`typeID` AND unit.unitID = `type-product`.`unitID` " +
                "AND product.productID = `provider-product`.`productID` AND provider.providerID = " +
                "`provider-product`.`providerID` AND product.productID="+productID+" " +
                "And provider.providerID = "+providerID+";";
            Statement statement = connection.prepareStatement(sql);
//            statement.executeUpdate();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                product = new Product(productID, resultSet.getInt(4), resultSet.getInt(6),
                        resultSet.getString(1), resultSet.getString(5), resultSet.getString(7),
                        resultSet.getString(8), resultSet.getString(2), resultSet.getString(3));
            }
        return product;
    }
}
