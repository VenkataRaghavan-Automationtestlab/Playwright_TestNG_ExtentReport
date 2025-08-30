/**
 * Playwright Framework
 * <p>
 * Enum representing supported browser types in the framework.
 * Used to standardize browser selection and avoid hardcoding strings.
 * </p>
 *
 * <p><b>Supported Browsers:</b></p>
 * <ul>
 *   <li>CHROME → "chrome"</li>
 *   <li>FIREFOX → "firefox"</li>
 *   <li>WEBKIT → "webkit"</li>
 *   <li>MSEDGE → "msedge"</li>
 * </ul>
 *
 * <p>Provides helper methods to:
 * <ul>
 *   <li>Convert a string value into the appropriate enum type.</li>
 *   <li>Fetch the configured browser directly from {@code config.properties}.</li>
 * </ul>
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * BrowserTypeEnum browserType = BrowserTypeEnum.fromConfig();
 *
 * switch (browserType) {
 *     case FIREFOX -> playwright.firefox().launch();
 *     case WEBKIT -> playwright.webkit().launch();
 *     case MSEDGE -> playwright.chromium().launch(
 *                       new BrowserType.LaunchOptions().setChannel("msedge"));
 *     default -> playwright.chromium().launch(
 *                       new BrowserType.LaunchOptions().setChannel("chrome"));
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package base;

import utils.ConfigManager;

/**
 * Enum for supported browser types.
 * Provides conversion utilities and config-based initialization.
 */
public enum BrowserTypeEnum {
    /** Google Chrome browser (Chromium channel). */
    CHROME("chrome"),

    /** Mozilla Firefox browser. */
    FIREFOX("firefox"),

    /** WebKit browser (Safari engine). */
    WEBKIT("webkit"),

    /** Microsoft Edge browser (Chromium channel). */
    MSEDGE("msedge");

    /** String representation of the browser name. */
    private final String name;

    /**
     * Constructs a BrowserTypeEnum with its associated string name.
     *
     * @param name browser name (e.g., "chrome", "firefox")
     */
    BrowserTypeEnum(String name) {
        this.name = name;
    }

    /**
     * Returns the lowercase browser name.
     *
     * @return the browser name string
     */
    public String getName() {
        return name;
    }

    /**
     * Converts a string into the corresponding enum value.
     * If no match is found, defaults to {@link #CHROME}.
     *
     * @param value the string value to match (case-insensitive)
     * @return the matching BrowserTypeEnum
     */
    public static BrowserTypeEnum fromString(String value) {
        for (BrowserTypeEnum type : BrowserTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return CHROME; // default fallback
    }

    /**
     * Reads the browser type directly from config.properties.
     * Uses {@link utils.ConfigManager#get(String)} with key "browser".
     *
     * @return the BrowserTypeEnum from config file
     */
    public static BrowserTypeEnum fromConfig() {
        String browserName = ConfigManager.get("browser");
        return fromString(browserName);
    }
}
