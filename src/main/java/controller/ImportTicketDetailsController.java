package controller;

import businessLogicLayer.EmployeeB;
import businessLogicLayer.ImportTicketB;
import businessLogicLayer.LoginB;
import businessLogicLayer.Validation;
import entity.Employee;
import entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class ImportTicketDetailsController {
    private ObservableList<Product> listProduct;
    private EmployeeB employeeB;
    private Validation validation;
    private ImportTicketB importTicketB;
    private LoginB loginB;
    @FXML
    private DatePicker txtDate;
    @FXML
    private ComboBox<Employee> comboEmployee;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ImageView imageProduct;
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
    private TableColumn<Product, String> colType;
    @FXML
    private TableColumn<Product, String> colProvider;
    @FXML
    private ImageView validateDate;
    @FXML
    private ImageView validateEmployee;

    public ImportTicketDetailsController() throws SQLException, ClassNotFoundException {
        listProduct = FXCollections.observableArrayList();
        employeeB = new EmployeeB();
        validation = new Validation();
        importTicketB = new ImportTicketB();
        loginB = new LoginB();
    }

    @FXML
    void initialize() throws SQLException {
        comboEmployee.setItems(employeeB.getAllEmployee());

        numberCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(tableProduct.getItems().indexOf(param.getValue()) + 1)));

        colImportPrice.setCellValueFactory(new PropertyValueFactory<>("impPrice"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        colProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        colProvider.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));

//        tableProduct.setItems(listProduct);

    }

    @FXML
    void actionAddProduct(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ProductDetails.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
//        stage.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ProductDetailsController controller = loader.getController();
        stage.setOnHiding(event1 -> {
            if ((controller.getProduct() != null)) {
                listProduct.add(controller.getProduct());
                refreshTable();
            }
        });
        stage.show();
    }

    private void refreshTable() {
//        tableProduct.getItems().clear();
        tableProduct.setItems(listProduct);
//        System.out.println(listProduct.toString());
    }

    @FXML
    void actionDelProduct(ActionEvent event) {
        if (tableProduct.getSelectionModel().getSelectedItem() != null) {
            String r = Notification.ConfrimAlert("Thông báo", "Bạn có thực sự muốn xóa không?");
            if (r != null) {
                if (r.equals("Y")) {
                    Product product = tableProduct.getSelectionModel().getSelectedItem();
                    listProduct.remove(product);
                    Notification.informationAlert("Thông Báo", "Xóa thành công");
                } else {
                    Notification.informationAlert("Thông Báo", "Something wrong");
                }
            }
        } else {
            Notification.informationAlert("Thông báo", "Hãy chọn hàng để tiến hành xóa");
        }
    }

    @FXML
    void actionEditProduct(ActionEvent event) throws IOException {
        if (tableProduct.getSelectionModel().getSelectedItem() != null) {
            Product product = tableProduct.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ProductDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
//        stage.initStyle(StageStyle.UTILITY);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            ProductDetailsController controller = loader.getController();
            stage.setOnShowing(event1 -> {
                controller.loadType(product.getTypeID(), product.getType());
                controller.loadName(product.getName());
                controller.loadCode(product.getShortName());
                controller.loadDescription(product.getDescription());
                controller.loadPrice(product.getImpPrice());
                controller.loadQuantity(product.getQuantity());
                controller.loadProvider(product.getProviderID());
                controller.loadUnit(product.getUnitID(), product.getUnit());
                controller.loadImage(product.getImageLink());
                controller.isUpdate(true);
//                controller.loadProduct(product);
            });

            stage.setOnHiding(event12 -> {
                listProduct.remove(product);
                listProduct.add(controller.getProduct());
                refreshTable();
            });

            stage.show();
        } else {
            Notification.informationAlert("Thông báo", "Hãy chọn hàng để tiến hành cập nhật");
        }
    }

    @FXML
    void clickTable(MouseEvent event) {
        if (tableProduct.getSelectionModel().getSelectedItem() != null) {
            imageProduct.setImage(new Image(tableProduct.getSelectionModel().getSelectedItem().getImageLink()));
        }
    }

    @FXML
    void actionSubmit(ActionEvent event) throws SQLException {
        if (checkSubmit()) {
            if (addImportTicket()) {
                Notification.informationAlert("Thông báo", "Thêm thành công");
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                Notification.informationAlert("Thông báo", "Something wrong!!!!");
            }
        }
    }

    private boolean addImportTicket() throws SQLException {
        return importTicketB.addImportTicket(txtDate.getValue(), txtDescription.getText(), loginB.getSession().getSessionName(), comboEmployee.getSelectionModel().getSelectedItem().getId(), listProduct);
//        System.out.println(b);
//        System.out.println(listProduct.toString());
//        System.out.println(txtDate.getValue());
//        System.out.println(comboEmployee.getSelectionModel().getSelectedItem().toString());
    }

    private boolean checkSubmit() {
        Boolean b = true;

        if (txtDate.getValue() == null) {
            validation.showErrors(null, validateDate, "Không được để trống ngày");
            b = false;
        }
        if (comboEmployee.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(null, validateEmployee, "Nhân viên không được để trống");
            b = false;
        }
        if (listProduct.isEmpty()) {
            Notification.informationAlert("Thông báo", "Chưa có mặt hàng nào được thêm vào");
            b = false;
        }
        return b;
    }

    @FXML
    void actionCancel(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public ObservableList<Product> getListProduct() {
        return listProduct;
    }
}
