package dataAccessLayer;

import entity.Provider;

import java.sql.*;
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
        String sql = "SELECT p.providerID, p.contactID,p.name,p.shortName,p.address,p.description,c.phoneNumber,c.email,c.Fax,p.goodProvider,p.badProvider,p.active,SP_PROVIDER_GET_LIQUIDATE(p.providerID),SP_PROVIDER_GET_TOTALMONEY(p.providerID),c.bankAccount\n" +
                "\t\tfrom provider as p, contact as c WHERE p.contactID = c.contactID and p.deleted = false;";

        ResultSet resultSet = statement.executeQuery(sql);
        int providerID;
        int contactID;
        String name;
        String shortName;
        String address;
        String description;
        String phone;
        String mail;
        String fax;
        boolean goodProvider;
        boolean badProvider;
        boolean active;
        int liquidate;
        int totalMoney;
        String bankAccount;
        while (resultSet.next()) {
            providerID = resultSet.getInt(1);
            contactID = resultSet.getInt(2);
            name = resultSet.getString(3);
            shortName = resultSet.getString(4);
            address = resultSet.getString(5);
            description = resultSet.getString(6);
            phone = resultSet.getString(7);
            mail = resultSet.getString(8);
            fax = resultSet.getString(9);
            goodProvider = resultSet.getBoolean(10);
            badProvider = resultSet.getBoolean(11);
            active = resultSet.getBoolean(12);
            liquidate = resultSet.getInt(13);
            totalMoney = resultSet.getInt(14);
            bankAccount = resultSet.getString(15);

            Provider provider = new Provider(providerID, contactID, name, shortName, address, description, phone, mail,
                    fax, goodProvider, badProvider, active, liquidate, totalMoney, bankAccount);
            list.add(provider);
        }
        statement.close();
        return list;
    }

    public boolean checkDuplicatedProviderShortName(String shortName) throws SQLException {
        String sql = "SELECT providerID from provider WHERE shortName = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, shortName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public void addProvider(String name, String shortName, String address, String description,
                            String phone, String mail, String fax, boolean goodProvider,
                            boolean badProvider, String createdBy, String bankAccount) throws SQLException {
        String sql = "INSERT INTO `provider` (`providerID`, `name`, `shortName`, `address`, `contactID`, `goodProvider`, `badProvider`, `active`, `description`, `deleted`, `createdBy`, `createDate`, `modifiedBy`, `modifiedDate`) \n" +
                "VALUES (NULL, ?, ?, ?, ?, ?, ?, '1', ?, '0', ?, CURRENT_TIMESTAMP, NULL, NULL);";
        int contactID = addContact(phone, mail, null, fax, bankAccount);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, shortName);
        statement.setString(3, address);
        statement.setInt(4, contactID);
        statement.setBoolean(5, goodProvider);
        statement.setBoolean(6, badProvider);
        statement.setString(7, description);
        statement.setString(8, createdBy);

        statement.executeUpdate();
        statement.close();
    }

    private int addContact(String phone, String email, String socialNetwork, String fax, String bankAcoount) throws SQLException {
        String sql = "INSERT INTO `contact` (`contactID`, `phoneNumber`, `email`, `SocialNetwork`, `Fax`,`bankAccount`) VALUES (NULL, ?, ?, ?, ?,?)";
        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, phone);
        statement.setString(2, email);
        statement.setString(3, socialNetwork);
        statement.setString(4, fax);
        statement.setString(5, bankAcoount);

        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();

        int generatedKey = 0;
        if (resultSet.next()) {
            generatedKey = resultSet.getInt(1);
        }
        statement.close();

        return generatedKey;
    }

    public void updateProvider(String name, String shortName, String address, String description,
                               String phone, String mail, String fax, boolean goodProvider,
                               boolean badProvider, String modifiedBy, int providerID, int contactID, String bankAccount) throws SQLException {
        String sql = "UPDATE `provider` SET `name` = ?, `shortName` = ?, `address` = ?, `goodProvider` = ?, `badProvider` = ?, `active` = 1, `description` = ?, `modifiedBy` = ?, `modifiedDate` = CURRENT_TIMESTAMP\n" +
                " WHERE `provider`.`providerID` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, shortName);
        statement.setString(3, address);
        statement.setBoolean(4, goodProvider);
        statement.setBoolean(5, badProvider);
        statement.setString(6, description);
        statement.setString(7, modifiedBy);
        statement.setInt(8, providerID);

        statement.executeUpdate();
        updateContact(contactID, phone, mail, null, fax, bankAccount);
    }

    private void updateContact(int contactID, String phoneNumber, String email, String socialNetwork, String fax, String bankAccount) throws SQLException {
        String sql = "UPDATE `contact` SET `phoneNumber` = ?, `email` = ?, `SocialNetwork` = ?, `Fax` = ? , `bankAccount` = ? WHERE `contact`.`contactID` = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, phoneNumber);
        statement.setString(2, email);
        statement.setString(3, socialNetwork);
        statement.setString(4, fax);
        statement.setString(5, bankAccount);
        statement.setInt(6, contactID);

        statement.executeUpdate();
        statement.close();
    }

    public void deleteProvider(int provoderID, String modifiedBy) throws SQLException {
        String sql = "UPDATE `provider` SET `deleted` = 1 , `modifiedBy` = ?, `modifiedDate` = CURRENT_TIMESTAMP\n" +
                " WHERE `provider`.`providerID` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, modifiedBy);
        statement.setInt(2, provoderID);
        statement.executeUpdate();
        statement.close();
    }

    public void updateActive(int provoderID, String modifiedBy, boolean b) throws SQLException {
        String sql = "UPDATE `provider` SET `active` = ? , `modifiedBy` = ?, `modifiedDate` = CURRENT_TIMESTAMP\n" +
                " WHERE `provider`.`providerID` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBoolean(1, b);
        statement.setString(2, modifiedBy);
        statement.setInt(3, provoderID);
        statement.executeUpdate();
        statement.close();
    }

    public void liquidateHistory() {
        String sql = "SELECT liquidate.date, liquidate.money from liquidate ,`importticket-liquidate`\n" +
                "where liquidate.liquidateID = `importticket-liquidate`.`liquidateID` and `importticket-liquidate`.`providerID`=?";
    }
}
