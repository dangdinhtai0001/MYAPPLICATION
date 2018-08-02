package controller;

import businessLogicLayer.FinanceB;
import entity.Salary;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presentation.Notification;

import java.io.IOException;
import java.sql.SQLException;


public class FinanceController {
    private FinanceB financeB;
    private ObservableList<Salary> listSalary;
    private String selectedSalaryName, selectedSalaryMoney, selectedDescription;
    private int salaryId;
    private boolean isUpdateSalary;

    public FinanceController() {
        try {
            financeB = new FinanceB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField salaryTableFilter;

    @FXML
    private TableView<Salary> salaryTable;

    @FXML
    private TableColumn<Salary, String> salaryNumberCol;

    @FXML
    private TableColumn<Salary, String> salaryNameCol;

    @FXML
    private TableColumn<Salary, Integer> salaryBasicCol;

    @FXML
    private TableColumn<Salary, String> salaryDescriptionCol;

    @FXML
    void initialize() {
        try {
            //làm cột số thứ tự
            salaryNumberCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(String.valueOf(salaryTable.getItems().indexOf(param.getValue()) + 1)));
            salaryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            salaryBasicCol.setCellValueFactory(new PropertyValueFactory<>("basic"));
            salaryDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

            listSalary = financeB.getAllSalary();
            salaryTable.setItems(listSalary);
            salaryTable.setEditable(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Sự kiện filter bảng
        ObservableList<Salary> data = salaryTable.getItems();
        salaryTableFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                salaryTable.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Salary> subentries = FXCollections.observableArrayList();

            long count = (long) salaryTable.getColumns().size();
            for (int i = 0; i < salaryTable.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + salaryTable.getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(salaryTable.getItems().get(i));
                        break;
                    }
                }
            }
            salaryTable.setItems(subentries);
        });


    }


    @FXML
    void addSalary(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = loadSalaryDetails();
        stage.show();
        stage.setOnHiding(event1 -> refreshSalaryTable());
    }

    private Stage loadSalaryDetails() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/SalaryDetails.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.onShowingProperty();
        return stage;
    }

    private void refreshSalaryTable() {
//        System.out.println("Refresh");
        try {
            listSalary = financeB.getAllSalary();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        salaryTable.getItems().clear();
//        salaryTable.refresh();
        salaryTable.setItems(listSalary);
    }


    @FXML
    void refreshSalary(ActionEvent event) {
        refreshSalaryTable();
    }

    @FXML
    void deleteSalary(ActionEvent event) {

    }

    @FXML
    void updateSalary(ActionEvent event) throws IOException {
        if (isUpdateSalary) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SalaryDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            //Cho phép sự kiện khi show lên
            stage.onShowingProperty();

            //Muốn controller ko null thì phải gọi hàm getController sau khi gọi hàm loader.load();
            SalaryDetailsController controller = loader.getController();

            stage.setOnShowing(event1 -> {
                controller.setTxtName(selectedSalaryName);
                controller.setTxtMoney(selectedSalaryMoney);
                controller.setTxtDescription(selectedDescription);
                controller.setUpdate(true);
                controller.setTitle("Sửa Thông Tin Mức Lương");
                controller.setSalaryId(salaryId);
            });

            stage.setOnHiding(event12 -> refreshSalaryTable());
            stage.show();
        } else {
            Notification.informationAlert("Thông báo", "Chọn hàng để chỉnh sửa");
        }
    }

    @FXML
    void selectSalary(MouseEvent event) {
        if (event.getClickCount() == 1) {
            if (salaryTable.getSelectionModel().getSelectedItem() != null) {
                salaryId = salaryTable.getSelectionModel().getSelectedItem().getId();
                selectedSalaryName = salaryTable.getSelectionModel().getSelectedItem().getName();
                selectedSalaryMoney = String.valueOf(salaryTable.getSelectionModel().getSelectedItem().getBasic());
                selectedDescription = salaryTable.getSelectionModel().getSelectedItem().getDescription();
                isUpdateSalary = true;
            } else {
                isUpdateSalary = false;
            }
        }
    }

}
