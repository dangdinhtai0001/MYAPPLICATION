package businessLogicLayer;


import dataAccessLayer.FinanceDA;
import entity.Salary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class FinanceB {
    private FinanceDA financeDA ;

    public FinanceB() throws SQLException, ClassNotFoundException {
        this.financeDA =new FinanceDA();
    }

    public ObservableList<Salary> getAllEmployee() throws SQLException {
        return  FXCollections.observableArrayList(financeDA.getAllSalary());
    }
}
