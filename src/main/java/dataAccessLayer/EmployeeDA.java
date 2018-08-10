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
        statement.close();
        return employee;
    }

    public boolean checkDuplicatedAccount(String username) throws SQLException {
        String sql = "Select e.employeeID from employee e  where e.username = ? ;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        statement.close();
        return resultSet.next();
    }

    public List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String sql = "\tSELECT e.employeeID,e.name,e.gender,e.description,e.address,c.phoneNumber,c.SocialNetwork,s.basic,SP_EMPLOYEE_GET_BONUS(e.employeeID),SP_EMPLOYEE_GET_PUNISH(e.employeeID),e.dateOfBirth,e.dateOfBegin,e.dateOfEnd,e.imageLink,e.username,e.password,e.isAdmin,s.salaryID,c.contactID\n" +
                "\tfrom employee as e , salary as s, `employee-salary`, contact as c\n" +
                "\twhere s.salaryID = `employee-salary`.`salaryID`\n" +
                "\tand e.employeeID = `employee-salary`.`employeeID`\n" +
                "\tand e.deleted = false\n" +
                "\tand c.contactID = e.contactID;";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {


            Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8),
                    resultSet.getInt(9), resultSet.getInt(10), resultSet.getDate(11),
                    resultSet.getDate(12), resultSet.getDate(13), resultSet.getString(14),
                    resultSet.getString(15), resultSet.getString(16), resultSet.getBoolean(17),
                    resultSet.getInt(18), resultSet.getInt(19));
            employees.add(employee);
        }

        statement.close();
        return employees;
    }

    private int addContact(String phone, String email, String socialNetwork, String fax) throws SQLException {
        String sql = "INSERT INTO `contact` (`contactID`, `phoneNumber`, `email`, `SocialNetwork`, `Fax`) VALUES (NULL, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, phone);
        statement.setString(2, email);
        statement.setString(3, socialNetwork);
        statement.setString(4, fax);

        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();

        int generatedKey = 0;
        if (resultSet.next()) {
            generatedKey = resultSet.getInt(1);
        }
        statement.close();

        return generatedKey;
    }

    private int addEmployee(String name, String gender, Date dateOfBirth, String address, String phoneNumber,
                            String facebook, Date dateOfBegin, Date dateOfEnd, String imageLink, String username,
                            String password, boolean isAdmin, String createdBy) throws SQLException {

        int contactID = addContact(phoneNumber, null, facebook, null);
        String sql = "INSERT INTO `employee` (`employeeID`, `name`, `gender`, `dateOfBirth`, `address`, `contactID`," +
                "  `dateOfBegin`, `dateOfEnd`, `imageLink`, `username`, `password`, `isAdmin`, `description`, " +
                "`deleted`, `createBy`, `createDate`, `modifiedBy`, `modifiedDate`) VALUES (NULL, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, false, ?, " +
                "CURRENT_TIMESTAMP, NULL, NULL)";

        PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, gender);
        statement.setDate(3, dateOfBirth);
        statement.setString(4, address);
        statement.setInt(5, contactID);
        statement.setDate(6, dateOfBegin);
        statement.setDate(7, dateOfEnd);
        statement.setString(8, imageLink);
        statement.setString(9, username);
        statement.setString(10, password);
        statement.setBoolean(11, isAdmin);
        statement.setString(12, createdBy);

        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();

        int generatedKey = 0;
        if (resultSet.next()) {
            generatedKey = resultSet.getInt(1);
        }
        statement.close();

        return generatedKey;
    }

    public void addEmployeeSalary(String name, String gender, Date dateOfBirth, String address, String phoneNumber,
                                  String facebook, Date dateOfBegin, Date dateOfEnd, String imageLink, String username,
                                  String password, boolean isAdmin, String createdBy, int salaryId) throws SQLException {
        String sql = "INSERT INTO `employee-salary` (`employeeID`, `salaryID`) VALUES (?, ?)";

        int employeeId = addEmployee(name, gender, dateOfBirth, address, phoneNumber, facebook, dateOfBegin, dateOfEnd,
                imageLink, username, password, isAdmin, createdBy);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeeId);
        statement.setInt(2, salaryId);

        statement.executeUpdate();
        statement.close();
    }

    public void updateEmployee(String name, String gender, Date dateOfBirth, String address, String phoneNumber,
                               String facebook, Date dateOfBegin, Date dateOfEnd, String imageLink, String username,
                               String password, boolean isAdmin, String modifiedBy, int salaryId, int employeeId, int oldSalaryID, int contactID) throws SQLException {
        String sql = "UPDATE `employee` SET `name` = ?, `gender` = ?, `dateOfBirth` = ?, `address` = ?, " +
                "`contactID` = ?, `dateOfBegin` = ?, `dateOfEnd` = ?, `imageLink` = ?, `username` = ?, " +
                "`password` = ?, `isAdmin` = ?, `modifiedBy` = ?, `modifiedDate` = CURRENT_TIMESTAMP  " +
                "WHERE `employee`.`employeeID` = ?;\n" +
                "\n";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, gender);
        statement.setDate(3, dateOfBirth);
        statement.setString(4, address);
        statement.setInt(5, contactID);
        statement.setDate(6, dateOfBegin);
        statement.setDate(7, dateOfEnd);
        statement.setString(8, imageLink);
        statement.setString(9, username);
        statement.setString(10, password);
        statement.setBoolean(11, isAdmin);
        statement.setString(12, modifiedBy);
        statement.setInt(13, employeeId);

        statement.executeUpdate();
        statement.close();

        updateContact(contactID, phoneNumber, null, facebook, null);
        updateEmployeeSalary(employeeId, salaryId, oldSalaryID);
    }

    private void updateEmployeeSalary(int employeeId, int salaryId, int oldSalaryID) throws SQLException {
        String sql = "UPDATE `employee-salary` SET `employeeID` = ?, `salaryID` = ? " +
                "WHERE `employee-salary`.`employeeID` = ? AND `employee-salary`.`salaryID` = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeeId);
        statement.setInt(2, salaryId);
        statement.setInt(3, employeeId);
        statement.setInt(4, oldSalaryID);

        statement.executeUpdate();
        statement.close();
    }

    private void updateContact(int contactID, String phoneNumber, String email, String socialNetwork, String fax) throws SQLException {
        String sql = "UPDATE `contact` SET `phoneNumber` = ?, `email` = ?, `SocialNetwork` = ?, `Fax` = ? WHERE `contact`.`contactID` = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, phoneNumber);
        statement.setString(2, email);
        statement.setString(3, socialNetwork);
        statement.setString(4, fax);
        statement.setInt(5, contactID);

        statement.executeUpdate();
        statement.close();
    }

    public void deleteEmployee(int employeeId, int salaryID, String modifiedBy) throws SQLException {
        String sql = "UPDATE `employee` SET `deleted` = '1',`modifiedBy` = ? , `modifiedDate` =CURRENT_TIMESTAMP  " +
                " WHERE `employee`.`employeeID` = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeeId);
        statement.setString(2, modifiedBy);

        statement.executeUpdate();
        statement.close();

        deleteEmployeeSalary(employeeId, salaryID);
    }

    private void deleteEmployeeSalary(int employeeID, int salaryID) throws SQLException {
        String sql = "DELETE FROM `employee-salary` " +
                "WHERE `employee-salary`.`employeeID` = ? AND `employee-salary`.`salaryID` = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeeID);
        statement.setInt(2, salaryID);

        statement.executeUpdate();
        statement.close();
    }
}
