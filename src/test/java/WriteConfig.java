import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WriteConfig {
    public static void main(String[] args) {
        Properties properties = new Properties();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/resources/ConfigFile/Config.properties");
            properties.load(inputStream);
            System.out.println(properties.get("userDatabase"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
