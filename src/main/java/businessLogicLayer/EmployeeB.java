package businessLogicLayer;

import dataAccessLayer.EmployeeDA;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class EmployeeB {

    private EmployeeDA employeeDA;

    public EmployeeB() throws SQLException, ClassNotFoundException {
        employeeDA = new EmployeeDA();
    }

    public ObservableList<Employee> getAllEmployee() throws SQLException {
        return FXCollections.observableArrayList(employeeDA.getEmployees());
    }

    public String convertDateToString(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private java.sql.Date convertDatetoSQLDATE(LocalDate date) {
        try {
            return java.sql.Date.valueOf(date);
        } catch (NullPointerException e) {
            return null;
        }
//        return res;
    }

    public boolean addEmployee(String name, String gender, LocalDate dateOfBirth, String address, String phoneNumber,
                               String facebook, LocalDate dateOfBegin, LocalDate dateOfEnd, String imageLink, String username,
                               String password, boolean isAdmin, String createdBy, String salaryId) {
        try {
            employeeDA.addEmployeeSalary(name, gender, convertDatetoSQLDATE(dateOfBirth), address, phoneNumber, facebook,
                    convertDatetoSQLDATE(dateOfBegin), convertDatetoSQLDATE(dateOfEnd),
                    imageLink, username, password, isAdmin, createdBy, Integer.parseInt(salaryId));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public boolean updateEmployee(String name, String gender, LocalDate dateOfBirth, String address, String phoneNumber,
                                  String facebook, LocalDate dateOfBegin, LocalDate dateOfEnd, String imageLink, String username,
                                  String password, boolean isAdmin, String createdBy, String salaryId, int employeeID, int oldSalaryID, int contactID) {
        try {
            employeeDA.updateEmployee(name, gender, convertDatetoSQLDATE(dateOfBirth), address, phoneNumber, facebook,
                    convertDatetoSQLDATE(dateOfBegin), convertDatetoSQLDATE(dateOfEnd),
                    imageLink, username, password, isAdmin, createdBy, Integer.parseInt(salaryId), employeeID, oldSalaryID, contactID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(int employeeId, int salaryID, String modifiedBy) {
        try {
            employeeDA.deleteEmployee(employeeId, salaryID, modifiedBy);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
