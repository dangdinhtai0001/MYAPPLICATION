package dataAccessLayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = null;
    private static InputStream inputStream = null;

    public static Properties getProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            // ko cần dùng đường dẫn tuyệt đối vì đã add vào file pom.xml (dòng 21 )
            inputStream = new FileInputStream("src/main/resources/ConfigFile/Config.properties");
            properties.load(inputStream);
        }
        return properties;
    }

    public static void closeInputStream() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
