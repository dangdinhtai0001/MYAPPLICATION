package dataAccessLayer;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUlti {
    private static Connection connection = null;
    private static Properties properties = null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            try {
                properties = Config.getProperties();
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = properties.getProperty("urlDatabase");
                String user = properties.getProperty("userDatabase");
                String password = properties.getProperty("passwordDatabase");
                connection = DriverManager.getConnection(url, user, password);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Config.closeInputStream();
            }

        }
        return connection;
    }
}