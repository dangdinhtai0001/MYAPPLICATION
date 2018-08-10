package controller;

import businessLogicLayer.EmployeeB;
import businessLogicLayer.LoginB;
import entity.Employee;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presentation.Notification;

import java.io.IOException;
import java.sql.SQLException;


public class EmployeeController {

    private EmployeeB employeeB;
    private ObservableList listEmployee;
    private boolean isUpdating;
    private LoginB loginB;
    @FXML
    private TableColumn<Employee, String> employeeGender;

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


    public EmployeeController() {
        try {
            employeeB = new EmployeeB();
            loginB = new LoginB();
        } catch (SQLException e) {
//            e.printStackTrace();
            Notification.showErors(e, "EmployeeController.Constructor");
        } catch (ClassNotFoundException e) {
            Notification.showErors(e, "EmployeeController.Constructor");
        }
    }

    @FXML
    void initialize() {
        isUpdating = false;

        try {
            //Dùng kế thừa thì cứ thêm hàm get vô
            employeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
            employeePhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            employeeGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

            listEmployee = employeeB.getAllEmployee();
            employeeTable.setItems(listEmployee);

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

            long count = (long) employeeTable.getColumns().size();
            for (int i = 0; i < employeeTable.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = String.valueOf(employeeTable.getColumns().get(j).getCellData(i));
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
        if (event.getClickCount() == 1 && !isUpdating) {
            if (employeeTable.getSelectionModel().getSelectedItem() != null) {
//            System.out.println(employeeTable.getSelectionModel().getSelectedItem().toString());
                lblName.setText(employeeTable.getSelectionModel().getSelectedItem().getName());
                lblGender.setText(employeeTable.getSelectionModel().getSelectedItem().getGender());
                lblDateOfBirth.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfBirth()));
                lblAddress.setText(employeeTable.getSelectionModel().getSelectedItem().getAddress());
                lblDateOfBegin.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfbegin()));
                lblDateOfEnd.setText(employeeB.convertDateToString(employeeTable.getSelectionModel().getSelectedItem().getDateOfEnd()));
                lblBonus.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getBonus()));
                lblPunish.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getPunish()));
                lblSalary.setText(String.valueOf(employeeTable.getSelectionModel().getSelectedItem().getBasicSalary()) + "/Tháng");
                lblPhone.setText(employeeTable.getSelectionModel().getSelectedItem().getPhoneNumber());
                lblFacebook.setText(employeeTable.getSelectionModel().getSelectedItem().getFacebook());
                lblUsername.setText(employeeTable.getSelectionModel().getSelectedItem().getUsername());
                lblPassword.setText(employeeTable.getSelectionModel().getSelectedItem().getPassword());
                isAdmin.setSelected(employeeTable.getSelectionModel().getSelectedItem().isAdmin());


                Image image = new Image(employeeTable.getSelectionModel().getSelectedItem().getImageLink());
                employeeImage.setImage(image);
            }
        }
    }

    @FXML
    void addEmployee(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EmployeeDetails.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        EmployeeDetailsController controller = loader.getController();
        controller.setTitle("Thêm nhân viên");
        stage.setOnHiding(event1 -> refreshEmployeeTable());
        stage.show();
    }

    private void refreshEmployeeTable() {
        try {
            listEmployee = employeeB.getAllEmployee();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        employeeTable.getItems().clear();
//        salaryTable.refresh();
        employeeTable.setItems(listEmployee);
    }


    @FXML
    void updateEmployee(ActionEvent event) throws IOException {
        if (employeeTable.getSelectionModel().getSelectedItem() != null) {
            isUpdating = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EmployeeDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            EmployeeDetailsController controller = loader.getController();
            controller.loadFacebook(lblFacebook.getText());
            controller.loadAccount(lblUsername.getText());
            controller.loadAddress(lblAddress.getText());
            controller.loadImage(employeeImage.getImage());
            controller.loadPassword(lblPassword.getText());
            controller.loadName(lblName.getText());
            controller.loadPhone(lblPhone.getText());
            controller.isUpdate(true);
            controller.loadGender(lblGender.getText());
            controller.loadSalary(employeeTable.getSelectionModel().getSelectedItem().getSalaryID());
            controller.loadDateOfBirth(employeeTable.getSelectionModel().getSelectedItem().getDateOfBirth());
            controller.loadDateOfBegin(employeeTable.getSelectionModel().getSelectedItem().getDateOfbegin());
            controller.loadDateOfEnd(employeeTable.getSelectionModel().getSelectedItem().getDateOfEnd());
            controller.isAdmin(employeeTable.getSelectionModel().getSelectedItem().isAdmin());
            controller.loadEmployeeID(employeeTable.getSelectionModel().getSelectedItem().getId());
            controller.loadOldSalaryID(employeeTable.getSelectionModel().getSelectedItem().getSalaryID());
            controller.loadUrl(employeeTable.getSelectionModel().getSelectedItem().getImageLink());
            controller.loadContactID(employeeTable.getSelectionModel().getSelectedItem().getContactID());

            controller.setTitle("Cập nhật thông tin");
            stage.setOnHiding(event1 -> {
                refreshEmployeeTable();
                isUpdating = false;
            });
            stage.show();
        } else {
            Notification.informationAlert("Thông báo", "Hãy chọn nhân viên để tiến hành cập nhật");
        }
    }


    @FXML
    void deleteEmployee(ActionEvent event) {
        if (employeeTable.getSelectionModel().getSelectedItem() != null) {
            String r = Notification.ConfrimAlert("Thông báo", "Bạn có thực sự muốn xóa không?");
            if (r != null) {
                if (r.equals("Y")) {
                    if (employeeB.deleteEmployee(employeeTable.getSelectionModel().getSelectedItem().getId(),
                            employeeTable.getSelectionModel().getSelectedItem().getSalaryID(), loginB.getSession().getSessionName())) {
                        Notification.informationAlert("Thông Báo", "Xóa thành công");
                        refreshEmployeeTable();
                    } else {
                        Notification.informationAlert("Thông Báo", "Something wrong");
                    }
                }
            }
        } else {
            Notification.informationAlert("Thông Báo", "Hãy chọn hàng để xóa");
        }
    }


}
