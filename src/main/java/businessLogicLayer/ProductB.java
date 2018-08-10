package businessLogicLayer;

import dataAccessLayer.ProductDA;
import entity.Node;
import entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProductB {
    private ProductDA productDA;

    public ProductB() throws SQLException, ClassNotFoundException {
        productDA = new ProductDA();
    }

    public ObservableList<Product> getAllProduct() throws SQLException {
        return FXCollections.observableArrayList(productDA.getAllProduct());
    }

    public ObservableList<Node> getAllType() throws SQLException {
        return FXCollections.observableArrayList(productDA.getAllType());
    }

    public ObservableList<Node> getAllUnit() throws SQLException {
        return FXCollections.observableArrayList(productDA.getAllUnit());
    }

    public boolean checkDuplicatedProductCode(String code) throws SQLException {
        return productDA.checkDuplicatedShortName(code);
    }
}
