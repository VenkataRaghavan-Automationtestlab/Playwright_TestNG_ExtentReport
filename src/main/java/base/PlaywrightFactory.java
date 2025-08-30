/**
 * Playwright Framework
 * <p>
 * Factory class responsible for managing Playwright lifecycle,
 * browser launch options, and context creation.
 * </p>
 *
 * <p>This class ensures:</p>
 * <ul>
 *   <li>Singleton-style management of Playwright and Browser instances.</li>
 *   <li>Browser selection based on {@code config.properties}.</li>
 *   <li>Support for Chrome, Edge, Firefox, WebKit, and Chromium.</li>
 *   <li>Maximized window handling across browsers.</li>
 *   <li>Integration with {@link utils.ReportManager} for logging.</li>
 * </ul>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package base;

import com.microsoft.playwright.*;
import utils.ConfigManager;
import utils.ReportManager;

import java.awt.*;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central factory class for Playwright browser initialization and management.
 * <p>
 * Provides static methods to start/stop Playwright, launch browsers,
 * create contexts, and open new pages. Ensures only one Playwright
 * instance exists per test run.
 * </p>
 */
public class PlaywrightFactory {
    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    /** Playwright instance (singleton). */
    private static Playwright playwright;

    /** Active browser instance. */
    private static Browser browser;

    /** Whether the browser should start maximized. */
    private static boolean maximize;

    /** Configured browser name (chrome, firefox, webkit, msedge). */
    private static String browserName;

    // --- Lifecycle ---

    /**
     * Initializes Playwright and launches the configured browser.
     * <p>
     * Reads configuration from {@code config.properties}:
     * <ul>
     *   <li>{@code browser} → chrome, firefox, webkit, msedge</li>
     *   <li>{@code headless} → true/false</li>
     *   <li>{@code maximize.window} → true/false</li>
     * </ul>
     * </p>
     *
     * @throws IllegalArgumentException if an unsupported browser is configured
     */
    public static synchronized void start() {
        if (playwright != null) return;

        playwright = Playwright.create();
        browserName = ConfigManager.get("browser").trim().toLowerCase();
        boolean headless = ConfigManager.getBoolean("headless");
        maximize = ConfigManager.getBoolean("maximize.window");

        BrowserType browserType = selectBrowserType();
        BrowserType.LaunchOptions launchOptions = buildLaunchOptions(headless);

        logBoth("Launching browser: {} | Headless: {} | Maximize: {}", browserName, headless, maximize);
        browser = browserType.launch(launchOptions);
    }

    /**
     * Returns the active browser instance.
     *
     * @return the Playwright Browser
     */
    public static Browser getBrowser() {
        if (browser == null) start();
        return browser;
    }

    /**
     * Creates a new browser context with viewport settings.
     *
     * @return a new BrowserContext
     */
    public static BrowserContext getContext() {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        if (maximize) {
            if (isFirefoxOrWebkit()) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                contextOptions.setViewportSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
                logBoth("Viewport set to screen size: {}x{}", (int) screenSize.getWidth(), (int) screenSize.getHeight());
            } else {
                contextOptions.setViewportSize(null); // Chromium browsers
                logBoth("Chromium-based browser → viewport set to NULL (real maximize).");
            }
        } else {
            logBoth("Using default viewport (not maximized).");
        }

        return browser.newContext(contextOptions);
    }

    /**
     * Opens a new page within a fresh browser context.
     *
     * @return a new Page instance
     */
    public static Page newPage() {
        return getContext().newPage();
    }

    /**
     * Closes the active browser and Playwright instance.
     * Ensures clean shutdown after test execution.
     */
    public static synchronized void stop() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
        logBoth("Browser and Playwright stopped.");
    }

    // --- Helpers ---

    /**
     * Selects the appropriate Playwright BrowserType based on config.
     *
     * @return the BrowserType instance
     */
    private static BrowserType selectBrowserType() {
        switch (browserName) {
            case "firefox":
                return playwright.firefox();
            case "webkit":
                return playwright.webkit();
            case "chrome":
            case "msedge":
            case "chromium":
                return playwright.chromium();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }

    /**
     * Builds browser launch options including headless, channel, and maximize args.
     *
     * @param headless whether to run in headless mode
     * @return configured LaunchOptions
     */
    private static BrowserType.LaunchOptions buildLaunchOptions(boolean headless) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        if ("chrome".equals(browserName)) {
            options.setChannel("chrome");
        } else if ("msedge".equals(browserName)) {
            options.setChannel("msedge");
        }

        if (maximize && isChromeBased()) {
            options.setArgs(Arrays.asList("--start-maximized"));
        }
        return options;
    }

    /**
     * Checks if the current browser is Chromium-based (Chrome/Edge).
     *
     * @return true if Chrome/Edge, false otherwise
     */
    private static boolean isChromeBased() {
        return "chrome".equals(browserName);
    }

    /**
     * Checks if the current browser is Firefox or WebKit.
     *
     * @return true if Firefox/WebKit, false otherwise
     */
    private static boolean isFirefoxOrWebkit() {
        return "firefox".equals(browserName) || "webkit".equals(browserName) || "chromium".equals(browserName);
    }

    /**
     * Logs messages to both SLF4J and ExtentReports.
     *
     * @param message log message (with placeholders "{}")
     * @param args    arguments to replace in the message
     */
    private static void logBoth(String message, Object... args) {
        log.info(message, args);
        try {
            ReportManager.logInfo(String.format(message.replace("{}", "%s"), args));
        } catch (Exception ignored) {
            // ReportManager may not be initialized early
        }
    }
}
