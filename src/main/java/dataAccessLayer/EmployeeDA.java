package dataAccessLayer;

import entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDA {
    private Connection connection;

    public EmployeeDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    public Employee checkLogin(String username, String password) throws SQLException {
        String sql = "Select e.employeeID,e.isAdmin from employee e  where e.username = ? and e.password = ? ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        Employee employee = null;
        while (resultSet.next()) {
            employee = new Employee(resultSet.getInt(1), username, password, resultSet.getBoolean(2));
        }
        return employee;
    }

    public List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT e.employeeID,e.name,e.gender,e.description,e.address,e.phoneNumber,e.facebook,s.basic,e.bonus,e.punish,e.dateOfBirth,e.dateOfBegin,e.dateOfEnd,e.imageLink,e.username,e.password,e.isAdmin\n" +
                "from employee as e , salary as s \n" +
                "where e.salaryID = s.salaryID and e.deleted = false;";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8),
                    resultSet.getInt(9), resultSet.getInt(10), resultSet.getDate(11),
                    resultSet.getDate(12), resultSet.getDate(13), resultSet.getString(14),
                    resultSet.getString(15), resultSet.getString(16), resultSet.getBoolean(17));
            employees.add(employee);
        }

        return employees;
    }
}
