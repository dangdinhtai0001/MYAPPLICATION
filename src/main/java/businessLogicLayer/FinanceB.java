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

    public ObservableList<Salary> getAllSalary() throws SQLException {
        return  FXCollections.observableArrayList(financeDA.getAllSalary());
    }

    public boolean checkDuplicatedSalary(String name, String money) throws SQLException {
        return financeDA.checkDuplicatedSalary(name, Integer.parseInt(money));
    }

    public void addSalary(String name, String money, String description, String createdBy) throws SQLException {
        financeDA.addSalary(name, Integer.parseInt(money), description, createdBy);
    }

    public void updateSalary(Integer id, String name, String money, String description, String modifiedBy) throws SQLException {
        financeDA.updateSalary(id, name, Integer.parseInt(money), description, modifiedBy);
    }
}
