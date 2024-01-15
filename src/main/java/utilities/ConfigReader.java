package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Static instance to hold properties across the class
    private static Properties properties;

    static {
        // Static block that runs before any other code
        String dosyaYolu = "configuration.properties";
        try {
            // Read properties from the configuration file
            FileInputStream fis = new FileInputStream(dosyaYolu);
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            // Handle exception if the properties file cannot be read
            System.out.println("properties dosyasi okunamadi");
        }
    }

    /**
     * Gets the value associated with the given key from the properties.
     *
     * @param key The key to look up in the properties.
     * @return The value associated with the key, or null if the key is not found.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets the value for the given key in the properties and updates the configuration file.
     *
     * @param key   The key for which the value is set.
     * @param value The value to be set for the key.
     */
    public static void setProperty(String key, String value) {
        try {
            // Write the updated properties to the configuration file
            FileOutputStream fos = new FileOutputStream("configuration.properties");
            properties.setProperty(key, value);
            properties.store(fos, null);
            fos.close();
        } catch (IOException e) {
            // Handle exception if there is an error while setting the property
            System.out.println("Error while setting property: " + e.getMessage());
        }
    }
}
