package controller;

import businessLogicLayer.ProductB;
import entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductController {
    private ObservableList<Product> listProduct;
    private ProductB productB;
    @FXML
    private TextField txtFilter;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<Product> tableProduct;
    @FXML
    private TableColumn<Product, Number> numberCol;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colUnit;
    @FXML
    private TableColumn<Product, Number> colQuantity;
    @FXML
    private TableColumn<Product, Number> colImportPrice;
    @FXML
    private TableColumn<Product, Number> colExportPrice;
    @FXML
    private TableColumn<Product, String> colProvider;
    @FXML
    private ImageView imageProduct;
    @FXML
    private TableColumn<Product, String> colType;
    @FXML
    private TableColumn<Product, Boolean> colLittleQuantity;
    @FXML
    private PieChart pieChart;

    public ProductController() {
        try {
            productB = new ProductB();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() throws SQLException {
        numberCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(tableProduct.getItems().indexOf(param.getValue()) + 1)));

        colExportPrice.setCellValueFactory(new PropertyValueFactory<>("expPrice"));
        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("impPrice"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        colProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        colProvider.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));

        //Làm cột có checkbox
        //đổi từ boolean sang booleanProperty
        colLittleQuantity.setCellValueFactory(param -> {
            Product product = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(product.getQuantity() < 10);
            return booleanProp;
        });
        //Tạo checkbox
        colLittleQuantity.setCellFactory(param -> {
            CheckBoxTableCell<Product, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        //Phải có đủ 2 đoạn này

        listProduct = productB.getAllProduct();
        tableProduct.setItems(listProduct);

        //Sự kiện filter bảng
        ObservableList<Product> data = tableProduct.getItems();
        txtFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tableProduct.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Product> subentries = FXCollections.observableArrayList();

            long count = (long) tableProduct.getColumns().size();
            for (int i = 0; i < tableProduct.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = String.valueOf(tableProduct.getColumns().get(j).getCellData(i));
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tableProduct.getItems().get(i));
                        break;
                    }
                }
            }
            tableProduct.setItems(subentries);
        });

        initChart();

    }

    private void initChart() {
        Map<String, Integer> map = new HashMap<>();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Product product : listProduct) {
            if (map.get(product.getType()) == null) {
                map.put(product.getType(), product.getQuantity());
            } else {
                map.put(product.getType(), map.get(product.getType()) + product.getQuantity());
            }
        }
        for (String key : map.keySet()) {
            pieChartData.add(new PieChart.Data(key, map.get(key)));
        }
        pieChart.setData(pieChartData);
    }

    @FXML
    void clickTable(MouseEvent event) {
        try {
            if (tableProduct.getSelectionModel().getSelectedItem() != null) {
                imageProduct.setImage(new Image(tableProduct.getSelectionModel().getSelectedItem().getImageLink()));
            }
        } catch (IllegalArgumentException e) {
            imageProduct.setImage(new Image("/ProductImage/Minions.gif"));
        }
    }

    @FXML
    void addNewProduct(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ImportTicketDetails.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnHiding(event1 -> {
            try {
                refreshTable();
                ImportTicketDetailsController controller = loader.getController();
                System.out.println(controller.getListProduct().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        stage.show();
    }

    private void refreshTable() throws SQLException {
        tableProduct.getItems().clear();
        listProduct = productB.getAllProduct();
        tableProduct.setItems(listProduct);
    }
}

