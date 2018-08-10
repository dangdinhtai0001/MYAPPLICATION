package controller;

import businessLogicLayer.ProductB;
import businessLogicLayer.ProviderB;
import entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class QuotationController {
    private ProductB productB;
    private ProviderB providerB;
    private ObservableList<Product> listProduct;
    private int providerID;
    @FXML
    private TextField txtFilter;
    @FXML
    private TableView<Product> tableQuotation;
    @FXML
    private TableColumn<Product, Number> colNumber;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colType;
    @FXML
    private TableColumn<Product, Number> colPrice;

    public QuotationController() throws SQLException, ClassNotFoundException {
        productB = new ProductB();
        providerB = new ProviderB();
    }

    @FXML
    void initialize() {
        colNumber.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(tableQuotation.getItems().indexOf(param.getValue()) + 1)));

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("impPrice"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

//        listProduct = providerB.getQuotation(providerID);
//        tableQuotation.setItems(listProduct);

        //Sự kiện filter bảng
        ObservableList<Product> data = tableQuotation.getItems();
        txtFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tableQuotation.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Product> subentries = FXCollections.observableArrayList();

            long count = (long) tableQuotation.getColumns().size();
            for (int i = 0; i < tableQuotation.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = String.valueOf(tableQuotation.getColumns().get(j).getCellData(i));
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tableQuotation.getItems().get(i));
                        break;
                    }
                }
            }
            tableQuotation.setItems(subentries);
        });

    }

    void loadProviderID(int i) {
        providerID = i;
        listProduct = providerB.getQuotation(providerID);
        tableQuotation.setItems(listProduct);
    }
}
