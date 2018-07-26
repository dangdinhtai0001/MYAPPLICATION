import dataAccessLayer.EmployeeDA;
import entity.Employee;

import java.sql.SQLException;
import java.util.List;

public class testEmployeeDA {
    public static void main(String[] args) {
        try {
            EmployeeDA employeeDA = new EmployeeDA();
            List<Employee> list = employeeDA.getEmployees();
            for (Employee e : list) {
//                System.out.println(e.);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
