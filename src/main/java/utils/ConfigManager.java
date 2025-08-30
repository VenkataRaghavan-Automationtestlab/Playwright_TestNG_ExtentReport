/**
 * Playwright Framework - ConfigManager
 * <p>
 * Utility class for loading and managing framework configuration
 * from {@code src/test/resources/config.properties}.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Loads properties at class initialization.</li>
 *   <li>Provides typed getters for String and boolean values.</li>
 *   <li>Generates a timestamped report directory path for each run.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * String browser = ConfigManager.get("browser");
 * boolean headless = ConfigManager.getBoolean("headless");
 * String reportPath = ConfigManager.getReportDir();
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * ConfigManager is responsible for loading framework configuration
 * values from the {@code config.properties} file.
 * <p>
 * Properties are loaded once (static initializer block) and can be
 * accessed throughout the test framework.
 * </p>
 */
public class ConfigManager {
    /** Properties object holding all loaded configuration. */
    private static Properties props = new Properties();

    /** Timestamped directory for storing reports. */
    private static String reportDir;

    // --- Static Initialization ---

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);

            // Create timestamped report folder path
            String baseDir = props.getProperty("report.dir", "reports");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportDir = baseDir + "/run_" + timestamp;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }

    // --- Getters ---

    /**
     * Retrieves a property value as a String.
     *
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Retrieves a property value as a boolean.
     *
     * @param key the property key
     * @return {@code true} if the value equals "true" (case-insensitive), else {@code false}
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Returns the report directory for the current run.
     * The folder path is dynamically generated with a timestamp.
     *
     * @return the full path to the report directory
     */
    public static String getReportDir() {
        return reportDir;
    }
}
