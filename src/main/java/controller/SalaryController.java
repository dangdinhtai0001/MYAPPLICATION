package controller;

import businessLogicLayer.SalaryB;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
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
import java.util.Map;

public class SalaryController {
    private SalaryB salaryB;
    private ObservableList<Salary> listSalary;
    private String selectedSalaryName, selectedSalaryMoney, selectedDescription;
    private int salaryId;
    private boolean isUpdateSalary;
    private Integer percentage;
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
    private PieChart salaryPieChart;
    @FXML
    private Label pieChartLabel;

    public SalaryController() {
        try {
            salaryB = new SalaryB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        try {
            //làm cột số thứ tự
            salaryNumberCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(String.valueOf(salaryTable.getItems().indexOf(param.getValue()) + 1)));
            salaryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            salaryBasicCol.setCellValueFactory(new PropertyValueFactory<>("basic"));
            salaryDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

            listSalary = salaryB.getAllSalary();
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
                    String entry = String.valueOf(salaryTable.getColumns().get(j).getCellData(i));
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(salaryTable.getItems().get(i));
                        break;
                    }
                }
            }
            salaryTable.setItems(subentries);
        });


        //Đẩy dữ liệu lên pie Chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        percentage = 0;
        for (Salary salary : listSalary) {
//            System.out.println(salary.getName() + "\n" +(Integer) getPieData().get(salary.getId()) );
            if (getPieData().get(salary.getId()) == null) {
                pieChartData.add(new PieChart.Data(salary.getName(), 0));
            } else {
                pieChartData.add(new PieChart.Data(salary.getName(), (Integer) getPieData().get(salary.getId())));
                percentage += (Integer) getPieData().get(salary.getId());
            }
        }
        salaryPieChart.setData(pieChartData);

        // sự kiện pieChart
        for (final PieChart.Data data1 : salaryPieChart.getData()) {
            data1.getNode().addEventHandler(MouseEvent.ANY,
                    e -> pieChartLabel.setText(String.valueOf((data1.getPieValue() / percentage) * 100) + "%")
            );
        }
    }

    private Map<Number, Number> getPieData() {
        return salaryB.countEmployee();
    }


    @FXML
    void addSalary(ActionEvent event) throws IOException {
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
            listSalary = salaryB.getAllSalary();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        salaryTable.getItems().clear();
//        salaryTable.refresh();
        salaryTable.setItems(listSalary);
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
