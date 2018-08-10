package dataAccessLayer;

import entity.Product;

import java.sql.*;
import java.util.List;

public class ImportTicketDA {
    private Connection connection;

    public ImportTicketDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    private int createImportTicketID(Date date, String description, String createdBy, int employeeID) throws SQLException {
        String sql = "INSERT INTO `importticket` (`importTicketID`,`employeeID`, `date`, `description`, `deleted`, `createdBy`, `createDate`, `modifiedBy`, `modifiedDate`)\n" +
                "VALUES (NULL, ? ,?, ?, '0', ?, CURRENT_TIMESTAMP, NULL, NULL);";

        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setInt(1, employeeID);
        statement.setDate(2, date);
        statement.setString(3, description);
        statement.setString(4, createdBy);

        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();

        int generatedKey = 0;
        if (resultSet.next()) {
            generatedKey = resultSet.getInt(1);
        }
        statement.close();

        return generatedKey;
    }

    public void addImportTicket(Date date, String description, String createdBy, int employeeID, List<Product> products) throws SQLException {
        int importTicketID = createImportTicketID(date, description, createdBy, employeeID);
        String sql = "INSERT INTO `importticket-product-provider` (`importTicketID`,`productID`,`providerID`,`quantity`,`price`) VALUES";
        String values = "";
        for (Product product : products) {
            values += " (" + String.valueOf(importTicketID) + "," + String.valueOf(product.getProductID()) + "," +
                    String.valueOf(product.getProviderID()) + "," + String.valueOf(product.getQuantity()) + "," +
                    String.valueOf(product.getImpPrice()) + ") , ";
        }

        values = values.substring(0, values.length() - 2) + ";";
        sql += values;

        Statement statement = connection.createStatement();
//        System.out.println(sql);
        statement.executeUpdate(sql);
        statement.close();
    }

}