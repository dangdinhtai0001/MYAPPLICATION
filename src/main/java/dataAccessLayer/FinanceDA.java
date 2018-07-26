package dataAccessLayer;

import entity.Salary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FinanceDA {
    private Connection connection;

    public FinanceDA() throws SQLException, ClassNotFoundException {
        connection = ConnectionUlti.getConnection();
    }

    public List<Salary> getAllSalary() throws SQLException {
        List<Salary> list = new ArrayList<>();

        String sql = "SELECT s.name,s.basic,s.description from salary s";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(new Salary(resultSet.getString(1),resultSet.getString(3),resultSet.getInt(2)));
        }
        return  list;
    }
}
