package businessLogicLayer;


import dataAccessLayer.SalaryDA;
import entity.Salary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Map;

public class SalaryB {
    private SalaryDA salaryDA;

    public SalaryB() throws SQLException, ClassNotFoundException {
        this.salaryDA = new SalaryDA();
    }

    public ObservableList<Salary> getAllSalary() throws SQLException {
        return FXCollections.observableArrayList(salaryDA.getAllSalary());
    }

    public boolean checkDuplicatedSalary(String name, String money) throws SQLException {
        return salaryDA.checkDuplicatedSalary(name, Integer.parseInt(money));
    }

    public void addSalary(String name, String money, String description, String createdBy) throws SQLException {
        salaryDA.addSalary(name, Integer.parseInt(money), description, createdBy);
    }

    public void updateSalary(Integer id, String name, String money, String description, String modifiedBy) throws SQLException {
        salaryDA.updateSalary(id, name, Integer.parseInt(money), description, modifiedBy);
    }

    public Map<Number, Number> countEmployee() {
        try {
            return salaryDA.countEmployee();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
