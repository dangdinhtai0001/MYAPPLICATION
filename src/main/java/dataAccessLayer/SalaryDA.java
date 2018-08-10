package dataAccessLayer;

import entity.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryDA {
    private Connection connection;

    public SalaryDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    public List<Salary> getAllSalary() throws SQLException {
        List<Salary> list = new ArrayList<>();

        String sql = "SELECT s.salaryId, s.name,s.basic,s.description from salary s where s.deleted = false;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(new Salary(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(4), resultSet.getInt(3)));
        }
        statement.close();
        return  list;
    }

    public boolean checkDuplicatedSalary(String name, Integer money) throws SQLException {
        String sql = "SELECT s.salaryID FROM salary as s WHERE s.name = ? and s.basic = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, money);
        ResultSet resultSet = statement.executeQuery();
//        statement.close();
        return !resultSet.next();
    }

    public void addSalary(String name, Integer money, String description, String createdBy) throws SQLException {
        String sql = "INSERT INTO `salary` (`salaryID`, `name`, `basic`, `description`, `deleted`, `createdBy`," +
                " `createDate`, `modifiedBy`, `modifiedDate`) VALUES (NULL, ?, ?, ?, '0', ?," +
                " CURRENT_TIMESTAMP, NULL, NULL);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, money);
        statement.setString(3, description);
        statement.setString(4, createdBy);

        statement.executeUpdate();
        statement.close();
    }

    public void updateSalary(Integer id, String name, Integer money, String description, String modifiedBy) throws SQLException {
        String sql = "UPDATE `salary` SET `name` = ?, `basic` = ?, `description` = ?, `modifiedBy` = ? ," +
                " `modifiedDate`= CURRENT_TIMESTAMP  WHERE `salary`.`salaryID` = ?; ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, money);
        statement.setString(3, description);
        statement.setString(4, modifiedBy);
        statement.setInt(5, id);

        statement.executeUpdate();
        statement.close();
    }

    public Map<Number, Number> countEmployee() throws SQLException {
        Map<Number, Number> map = new HashMap<>();
        String sql = "SELECT `employee-salary`.`salaryID`, COUNT(employee.employeeID) FROM `employee`, `employee-salary`  \n" +
                "WHERE employee.employeeID = `employee-salary`.`employeeID` GROUP by `employee-salary`.`salaryID`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            map.put(resultSet.getInt(1), resultSet.getInt(2));
        }
        statement.close();
        return map;
    }
}
