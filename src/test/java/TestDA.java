import dataAccessLayer.ProductDA;
import entity.Product;

import java.sql.SQLException;
import java.util.List;

public class TestDA {
    public static void main(String[] args) {
//        try {
//            ProviderDA employeeDA = new EmployeeDA();
//            List<Employee> list = employeeDA.getEmployees();
//            for (Employee e : list) {
////                System.out.println(e.);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        try {
//            ProviderDA providerDA = new ProviderDA();
//            List<Provider> list = providerDA.getAllProvider();
//            for (Provider provider: list) {
//                System.out.println(provider.toString());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        try {
            ProductDA productDA = new ProductDA();
            List<Product> list = productDA.getAllProduct();
            for (Product product : list) {
                System.out.println(product.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
