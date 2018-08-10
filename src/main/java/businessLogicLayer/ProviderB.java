package businessLogicLayer;

import dataAccessLayer.ProductDA;
import dataAccessLayer.ProviderDA;
import entity.Product;
import entity.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProviderB {
    private ProviderDA providerDA;
    private ProductDA productDA;

    public ProviderB() throws SQLException, ClassNotFoundException {
        providerDA = new ProviderDA();
        productDA = new ProductDA();
    }

    public ObservableList<Provider> getAllProvider() throws SQLException {
        return FXCollections.observableArrayList(providerDA.getAllProvider());
    }

    public boolean checkDuplicatedShortName(String shortName) throws SQLException {
        return providerDA.checkDuplicatedProviderShortName(shortName);
    }

    public boolean addProvider(String name, String shortName, String address, String description,
                               String phone, String mail, String fax, boolean goodProvider,
                               boolean badProvider, String createdBy, String bankAccount) {
        try {
            providerDA.addProvider(name, shortName, address, description, phone, mail, fax, goodProvider, badProvider, createdBy, bankAccount);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProvider(String name, String shortName, String address, String description,
                                  String phone, String mail, String fax, boolean goodProvider,
                                  boolean badProvider, String modifiedBy, int providerID, int contactID, String bankAccount) {
        try {
            providerDA.updateProvider(name, shortName, address, description, phone, mail, fax, goodProvider,
                    badProvider, modifiedBy, providerID, contactID, bankAccount);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteProvider(int provoderID, String modifiedBy) {
        try {
            providerDA.deleteProvider(provoderID, modifiedBy);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateActive(int provoderID, String modifiedBy, boolean b) {
        try {
            providerDA.updateActive(provoderID, modifiedBy, b);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Product> getQuotation(int providerID) {
        try {
            return FXCollections.observableArrayList(productDA.getQuotation(providerID));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
