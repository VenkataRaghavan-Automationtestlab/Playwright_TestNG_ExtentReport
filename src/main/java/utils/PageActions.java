/**
 * Playwright Framework - PageActions
 * <p>
 * Utility wrapper around Playwright {@link com.microsoft.playwright.Page} that provides
 * reusable actions (click, type, select, wait, etc.) with logging and screenshots.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Encapsulates Playwright {@link com.microsoft.playwright.Locator}-based actions.</li>
 *   <li>Automatically logs each action into {@link utils.ReportManager} with screenshots.</li>
 *   <li>Provides retry-safe methods for handling flaky UI elements.</li>
 *   <li>Supports dropdowns, checkboxes, radio buttons, alerts, and multi-element operations.</li>
 *   <li>Convenient wait helpers for element visibility/hidden state.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * PageActions actions = new PageActions(page, "LoginTest");
 * actions.navigate("https://www.saucedemo.com");
 * actions.fill("#user-name", "standard_user");
 * actions.fill("#password", "secret_sauce");
 * actions.click("#login-button");
 * String title = actions.getText(".title");
 * Assert.assertEquals(title, "Products");
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

/**
 * Wrapper around Playwright's {@link Page} for performing
 * common UI interactions with logging and screenshots.
 */
public class PageActions {
    private final Page page;
    private final String prefix;

    /**
     * Constructor to initialize PageActions.
     *
     * @param page   the Playwright {@link Page} instance
     * @param prefix test name or step prefix for screenshots
     */
    public PageActions(Page page, String prefix) {
        this.page = page;
        this.prefix = prefix;
    }

    // --- Logging helper with screenshot ---
    private void logEachAction(String desc) {
        try {
            String ss = ScreenshotUtil.takeScreenshot(page, prefix + "_" + desc.replaceAll("\\s+", "_"));
            ReportManager.stepPass(desc, ss);
        } catch (Exception e) {
            ReportManager.logInfo("Screenshot failed for: " + desc);
        }
    }

    // --- Navigation ---

    /**
     * Navigates to a URL.
     *
     * @param url the target URL
     */
    public void navigate(String url) {
        page.navigate(url);
        logEachAction("Navigate → " + url);
    }

    // --- Clicks ---

    /**
     * Clicks an element with auto-wait for visibility & enabled state.
     *
     * @param selector Playwright selector
     */
    public void click(String selector) {
        page.locator(selector).click();
        logEachAction("Click → " + selector);
    }

    /**
     * Retry-safe click for handling flaky UI elements.
     *
     * @param selector Playwright selector
     * @param retries  number of retry attempts
     */
    public void safeClick(String selector, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                page.locator(selector).click(new Locator.ClickOptions().setTimeout(3000));
                logEachAction("SafeClick → " + selector);
                return;
            } catch (Exception e) {
                if (i == retries - 1) throw e;
            }
        }
    }

    // --- Typing ---

    /**
     * Fills input field with text.
     *
     * @param selector Playwright selector
     * @param value    value to type
     */
    public void fill(String selector, String value) {
        page.locator(selector).fill(value);
        logEachAction("Fill → " + selector + " = " + value);
    }

    // --- Get Text ---

    /**
     * Gets the inner text of an element.
     *
     * @param selector Playwright selector
     * @return text content
     */
    public String getText(String selector) {
        String text = page.locator(selector).innerText();
        logEachAction("GetText → " + selector + " = " + text);
        return text;
    }

    // --- Waits ---

    /**
     * Waits until an element becomes visible.
     *
     * @param selector Playwright selector
     */
    public void waitForVisible(String selector) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        logEachAction("WaitForVisible → " + selector);
    }

    /**
     * Waits until an element becomes hidden.
     *
     * @param selector Playwright selector
     */
    public void waitForHidden(String selector) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        logEachAction("WaitForHidden → " + selector);
    }

    // --- Dropdowns ---

    /**
     * Selects an option from a dropdown by value attribute.
     *
     * @param selector Playwright selector
     * @param value    option value
     */
    public void selectByValue(String selector, String value) {
        page.locator(selector).selectOption(value);
        logEachAction("SelectByValue → " + selector + " = " + value);
    }

    /**
     * Selects an option from a dropdown by visible text.
     *
     * @param selector Playwright selector
     * @param text     option label
     */
    public void selectByText(String selector, String text) {
        page.locator(selector).selectOption(new SelectOption().setLabel(text));
        logEachAction("SelectByText → " + selector + " = " + text);
    }

    // --- Checkbox & Radio ---

    /** Checks a checkbox. */
    public void check(String selector) {
        page.locator(selector).check();
        logEachAction("Check → " + selector);
    }

    /** Unchecks a checkbox. */
    public void uncheck(String selector) {
        page.locator(selector).uncheck();
        logEachAction("Uncheck → " + selector);
    }

    /** Selects a radio button. */
    public void selectRadio(String selector) {
        page.locator(selector).check();
        logEachAction("SelectRadio → " + selector);
    }

    // --- Multi-elements ---

    /**
     * Gets all inner texts of matching elements.
     *
     * @param selector Playwright selector
     * @return list of texts
     */
    public List<String> getAllTexts(String selector) {
        List<String> texts = page.locator(selector).allInnerTexts();
        logEachAction("GetAllTexts → " + selector + " = " + texts);
        return texts;
    }

    // --- Assertions / State ---

    /** @return true if element is visible */
    public boolean isVisible(String selector) {
        boolean v = page.locator(selector).isVisible();
        logEachAction("IsVisible → " + selector + " = " + v);
        return v;
    }

    /** @return true if element is enabled */
    public boolean isEnabled(String selector) {
        boolean v = page.locator(selector).isEnabled();
        logEachAction("IsEnabled → " + selector + " = " + v);
        return v;
    }

    /** @return true if element is checked (checkbox/radio) */
    public boolean isChecked(String selector) {
        boolean v = page.locator(selector).isChecked();
        logEachAction("IsChecked → " + selector + " = " + v);
        return v;
    }

    // --- Alerts ---

    /** Accepts an alert dialog. */
    public void acceptAlert() {
        page.onceDialog(Dialog::accept);
        logEachAction("Accept Alert");
    }

    /** Dismisses an alert dialog. */
    public void dismissAlert() {
        page.onceDialog(Dialog::dismiss);
        logEachAction("Dismiss Alert");
    }

    /**
     * Types text into an alert dialog and accepts it.
     *
     * @param text text to type in alert
     */
    public void typeInAlert(String text) {
        page.onceDialog(dialog -> dialog.accept(text));
        logEachAction("TypeInAlert → " + text);
    }
}
