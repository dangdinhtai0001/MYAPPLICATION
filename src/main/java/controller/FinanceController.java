package controller;

import businessLogicLayer.FinanceB;
import entity.Salary;
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

public class FinanceController {
    private FinanceB financeB;

    public FinanceController() {
        try {
            financeB = new FinanceB();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
            salaryNumberCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(salaryTable.getItems().indexOf(param.getValue()) + 1)));
            salaryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            salaryBasicCol.setCellValueFactory(new PropertyValueFactory<>("basic"));
            salaryDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            salaryTable.setItems(financeB.getAllEmployee());
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

            long count = salaryTable.getColumns().stream().count();
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

}
