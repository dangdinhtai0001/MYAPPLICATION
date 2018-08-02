package businessLogicLayer;

import dataAccessLayer.EmployeeDA;
import entity.Employee;
import entity.Session;

import java.sql.SQLException;

public class LoginB {
    private static EmployeeDA employeeDA;
    private static Session session;

    public LoginB() throws SQLException, ClassNotFoundException {
        if (employeeDA == null) {
            employeeDA = new EmployeeDA();
        }
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        Employee employee = employeeDA.checkLogin(username, password);
        if (employee == null) {
            return false;
        } else {
            session = new Session(employee.getUsername(), employee.isAdmin());
            return true;
        }
    }

    public Session getSession() {
        return session;
    }

    public boolean checkDuplicatedAccount(String username) {
        try {
            return employeeDA.checkDuplicatedAccount(username);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
