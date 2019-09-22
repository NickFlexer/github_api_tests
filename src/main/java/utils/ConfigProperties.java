package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {

    private static Properties PROPERTIES;

    private static Properties readProperties(String fileName) {
        FileInputStream fis = null;
        Properties result = null;

        try {
            fis = new FileInputStream(fileName);
            result = new Properties();
            result.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    static {
        PROPERTIES = readProperties("./config.properties");
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
