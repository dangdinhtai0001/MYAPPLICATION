package businessLogicLayer;

import dataAccessLayer.ImportTicketDA;
import dataAccessLayer.ProductDA;
import entity.Product;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ImportTicketB {
    private ImportTicketDA importTicketDA;
    private ProductDA productDA;

    public ImportTicketB() throws SQLException, ClassNotFoundException {
        importTicketDA = new ImportTicketDA();
        productDA = new ProductDA();
    }

    private java.sql.Date convertDatetoSQLDATE(LocalDate date) {
        try {
            return java.sql.Date.valueOf(date);
        } catch (NullPointerException e) {
            return null;
        }
//        return res;
    }

    private List<Product> createProductID(List<Product> products, String createdBy) throws SQLException {
        for (Product product : products) {
            product.setProductID(productDA.addProduct(product, createdBy));
        }
        return products;
    }

    public boolean addImportTicket(LocalDate localDate, String description, String createdBy, int employeeID, List<Product> products) {
        try {
            java.sql.Date date = convertDatetoSQLDATE(localDate);
            // cáy ni phải chạy đúng thứ tự a ri
            createProductID(products, createdBy);
            importTicketDA.addImportTicket(date, description, createdBy, employeeID, products);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
