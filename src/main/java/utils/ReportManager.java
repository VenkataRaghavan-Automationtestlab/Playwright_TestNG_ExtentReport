/**
 * Playwright Framework
 * <p>
 * Utility class for managing ExtentReports lifecycle and logging.
 * Provides methods to initialize reports, create tests, log steps
 * (pass/fail/skip/info) and attach screenshots.
 * </p>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.nio.file.Path;

/**
 * Centralized manager for ExtentReports.
 * <p>
 * Handles report initialization, test creation, logging with screenshots,
 * and flushing reports to disk. Ensures thread-safe usage via
 * {@link ThreadLocal}.
 * </p>
 */
public class ReportManager {
    /** ExtentReports instance (singleton). */
    private static ExtentReports extent;

    /** Thread-local storage of ExtentTest for parallel test support. */
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    /** Report output directory (timestamped per run). */
    private static String reportDir;

    /**
     * Initializes the ExtentReports instance with a Spark reporter.
     * <p>
     * Report is created under {@code reportDir}/index.html where {@code reportDir}
     * is dynamically generated per run using {@link ConfigManager#getReportDir()}.
     * </p>
     * <p>System info includes:</p>
     * <ul>
     *   <li>Author (from {@code config.properties} → "name")</li>
     *   <li>Framework (Playwright Java)</li>
     *   <li>Environment (QA)</li>
     * </ul>
     */
    public static synchronized void initReports() {
        if (extent == null) {
            reportDir = ConfigManager.getReportDir(); // use timestamped folder
            new File(reportDir).mkdirs();
            ExtentSparkReporter spark = new ExtentSparkReporter(reportDir + "/index.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Author", ConfigManager.get("name"));
            extent.setSystemInfo("Framework", "Playwright Java");
            extent.setSystemInfo("Environment", "QA");
        }
    }

    /**
     * Creates a new ExtentTest for the given test name.
     *
     * @param name test method name
     * @return created ExtentTest instance
     */
    public static ExtentTest createTest(String name) {
        ExtentTest t = extent.createTest(name);
        test.set(t);
        return t;
    }

    /**
     * Returns the current thread's ExtentTest instance.
     *
     * @return current ExtentTest or null if none exists
     */
    public static ExtentTest getTest() {
        return test.get();
    }

    // --- Helpers ---

    /**
     * Converts an absolute screenshot path into a relative path
     * for embedding inside the report.
     *
     * @param absolutePath the screenshot absolute path
     * @return relative path string (POSIX style), or null if input is null
     */
    private static String getRelativePath(String absolutePath) {
        if (absolutePath == null) return null;
        Path abs = Path.of(absolutePath).toAbsolutePath().normalize();
        Path base = Path.of(reportDir).toAbsolutePath().normalize();
        Path rel = base.relativize(abs);
        return rel.toString().replace("\\", "/");
    }

    // --- Step Logging ---

    /**
     * Logs a passed step with optional screenshot.
     *
     * @param message        step description
     * @param screenshotPath optional screenshot file path
     */
    public static void stepPass(String message, String screenshotPath) {
        ExtentTest t = getTest();
        if (t == null) return;
        String relPath = getRelativePath(screenshotPath);
        if (relPath != null) {
            t.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(relPath).build());
        } else {
            t.pass(message);
        }
    }

    /**
     * Logs a failed step with optional screenshot.
     *
     * @param message        failure message
     * @param screenshotPath optional screenshot file path
     */
    public static void stepFail(String message, String screenshotPath) {
        ExtentTest t = getTest();
        if (t == null) return;
        String relPath = getRelativePath(screenshotPath);
        if (relPath != null) {
            t.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(relPath).build());
        } else {
            t.fail(message);
        }
    }

    /**
     * Logs a failure with screenshot (non-relative path variant).
     *
     * @param message        failure message
     * @param screenshotPath screenshot absolute path
     */
    public static void logFailWithScreenshot(String message, String screenshotPath) {
        if (getTest() != null) {
            if (screenshotPath != null) {
                getTest().fail(message).addScreenCaptureFromPath(screenshotPath);
            } else {
                getTest().fail(message);
            }
        }
    }

    /**
     * Logs a skipped test step.
     *
     * @param message skip message
     */
    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().skip(message);
        }
    }

    /**
     * Logs an informational step.
     *
     * @param message info message
     */
    public static void info(String message) {
        ExtentTest t = getTest();
        if (t != null) t.info(message);
    }

    /**
     * Logs a passed test or step.
     *
     * @param message pass message
     */
    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().pass(message);
        }
    }

    /**
     * Logs informational message (alias to {@link #info(String)}).
     *
     * @param message info message
     */
    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
    }

    /**
     * Flushes ExtentReports and writes the report to disk.
     * <p>Call this once after test execution completes.</p>
     */
    public static void flush() {
        if (extent != null) extent.flush();
    }
}
