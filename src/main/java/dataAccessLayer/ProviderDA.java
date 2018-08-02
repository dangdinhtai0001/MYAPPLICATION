package dataAccessLayer;

import entity.Provider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProviderDA {
    private Connection connection;

    public ProviderDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();

    }

    public List<Provider> getAllProvider() throws SQLException {
        List<Provider> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "SELECT provider.providerID ,provider.name,provider.address,provider.contact,provider.description,product.productID\n" +
                "FROM `provider` , `provider-product` , `product`" +
                "WHERE provider.providerID = `provider-product`.`providerID`" +
                "and `provider-product`.`productID` = product.productID;";
        ResultSet resultSet = statement.executeQuery(sql);
        int providerID = 0;
        String name = "", address = "", contact = "", description = "";
        ArrayList<Integer> productID = new ArrayList<>();
        while (resultSet.next()) {
            if (providerID == 0) {
                providerID = resultSet.getInt(1);
                name = resultSet.getString(2);
                address = resultSet.getString(3);
                contact = resultSet.getString(4);
                description = resultSet.getString(5);
                productID.add(resultSet.getInt(6));
            } else {
                if (providerID == resultSet.getInt(1)) {
                    productID.add(resultSet.getInt(6));
                } else {
                    Provider provider = new Provider(providerID, name, address, contact, description, productID);
                    list.add(provider);
                    productID = new ArrayList<>();
                    providerID = resultSet.getInt(1);
                    name = resultSet.getString(2);
                    address = resultSet.getString(3);
                    contact = resultSet.getString(4);
                    description = resultSet.getString(5);
                    productID.add(resultSet.getInt(6));

                    if (resultSet.next() == false) {
                        Provider last = new Provider(providerID, name, address, contact, description, productID);
                        list.add(last);
                    }
                }
            }
        }
        statement.close();
        return list;
    }
}
