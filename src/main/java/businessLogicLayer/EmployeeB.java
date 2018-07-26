package businessLogicLayer;

import dataAccessLayer.EmployeeDA;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeB {

    private EmployeeDA employeeDA ;

    public EmployeeB() throws SQLException, ClassNotFoundException {
        employeeDA = new EmployeeDA();
    }

    public ObservableList<Employee> getAllEmployee() throws SQLException {
        return  FXCollections.observableArrayList(employeeDA.getEmployees());
    }

    public String convertDateToString (Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
