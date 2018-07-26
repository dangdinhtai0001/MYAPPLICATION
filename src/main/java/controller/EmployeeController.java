package controller;

import businessLogicLayer.EmployeeB;
import entity.Employee;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import presentation.Notification;

import java.sql.SQLException;
import java.text.ParseException;


public class EmployeeController {

    private EmployeeB employeeB;

    public EmployeeController() {
        try {
            employeeB = new EmployeeB();
        } catch (SQLException e) {
//            e.printStackTrace();
            Notification.showErors(e, "EmployeeController.Constructor");
        } catch (ClassNotFoundException e) {
            Notification.showErors(e, "EmployeeController.Constructor");
        }
    }

    @FXML
    private HBox backGround;

    @FXML
    private TextField txtFilter;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> employeeNumber;

    @FXML
    private TableColumn<Employee, String> employeeName;

    @FXML
    private TableColumn<Employee, String> employeePhone;

    @FXML
    void initialize() {

        try {
            //Dùng kế thừa thì cứ thêm hàm get vô
            employeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
            employeePhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            employeeTable.setItems(employeeB.getAllEmployee());

            //làm cột số thứ tự
            employeeNumber.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(employeeTable.getItems().indexOf(param.getValue()) + 1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Sự kiện filter bảng
        ObservableList<Employee> data = employeeTable.getItems();
        txtFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                employeeTable.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Employee> subentries = FXCollections.observableArrayList();

            long count = employeeTable.getColumns().stream().count();
            for (int i = 0; i < employeeTable.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + employeeTable.getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(employeeTable.getItems().get(i));
                        break;
                    }
                }
            }
            employeeTable.setItems(subentries);
        });
    }

    @FXML
    private ImageView employeeImage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblGender;

    @FXML
    private Label lblDateOfBirth;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblDateOfBegin;

    @FXML
    private Label lblDateOfEnd;

    @FXML
    private Label lblBonus;

    @FXML
    private Label lblPunish;

    @FXML
    private Label lblSalary;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblFacebook;

    @FXML
    private Label lblUsername;

    @FXML
    private CheckBox isAdmin;

    @FXML
    private Label lblPassword;

    @FXML
    void clickItem(MouseEvent event) {
        if (event.getClickCount() == 1) {
//            System.out.println(employeeTable.getSelectionModel().getSelectedItem().toString());
            lblName.setText(employeeTable.getSelectionModel().getSelectedItem().getName());
            lblGender.setText(employeeTable.getSelectionModel().getSelectedItem().getGender());
            lblDateOfBirth.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfBirth()));
            lblAddress.setText(employeeTable.getSelectionModel().getSelectedItem().getAddress());
            lblDateOfBegin.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfbegin()));

            if (employeeTable.getSelectionModel().getSelectedItem().getDateOfEnd() == null) {
                lblDateOfEnd.setText("--Vẫn Đang Làm Việc");
            } else {
                lblDateOfEnd.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfEnd()));
            }
            lblBonus.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getBonus()));
            lblPunish.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getPunish()));
            lblSalary.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getBasicSalary()) + "/Tháng");
            if (employeeTable.getSelectionModel().getSelectedItem().getPhoneNumber() == null) {
                lblPhone.setText("--Không dùng điện thoại");
            } else {
                lblPhone.setText(employeeTable.getSelectionModel().getSelectedItem().getPhoneNumber());
            }
            if (employeeTable.getSelectionModel().getSelectedItem().getFacebook() != null) {
                lblFacebook.setText(employeeTable.getSelectionModel().getSelectedItem().getFacebook());
            } else {
                lblFacebook.setText("--Không sử dụng");
            }
            if (employeeTable.getSelectionModel().getSelectedItem().getUsername() == null) {
                lblUsername.setText("--Chưa Cấp");
            } else {
                lblUsername.setText(employeeTable.getSelectionModel().getSelectedItem().getUsername());
            }
            if (employeeTable.getSelectionModel().getSelectedItem().getPassword() == null) {
                lblPassword.setText("--Chưa Cấp");
            } else {
                lblPassword.setText(employeeTable.getSelectionModel().getSelectedItem().getPassword());
            }
            isAdmin.setSelected(employeeTable.getSelectionModel().getSelectedItem().isAdmin());



            Image image = new Image(employeeTable.getSelectionModel().getSelectedItem().getImageLink());
            employeeImage.setImage(image);
        }
    }
}
