/**
 * Playwright Framework
 * Copyright @ 2025 Perficient
 * @author Venkataraghavan
 */

/**
 * Playwright Framework
 * Base foundation for all Page Objects in the automation framework.
 * Provides a consistent way to access the underlying {@link com.microsoft.playwright.Page}
 * object and utility methods via {@link utils.PageActions}.
 */

package base;

import com.microsoft.playwright.Page;
import utils.PageActions;

/**
 * Base class for all page objects.
 * Provides Playwright Page + reusable PageActions.
 */
public abstract class BasePage {
    protected final Page page;
    protected final PageActions actions;
    
    /**
     * Constructs a BasePage instance.
     *
     * @param page     the Playwright {@link Page} object for this test
     * @param testName the current test name (used in logging and screenshots)
     */
    public BasePage(Page page, String testName) {
        this.page = page;
        this.actions = new PageActions(page, testName);
    }

    /**
     * Returns the current page title.
     *
     * @return the page title as a String
     */
    // Common utilities
    public String getPageTitle() {
        return page.title();
    }
    
    /**
     * Returns the current page URL.
     *
     * @return the page URL as a String
     */
    public String getPageUrl() {
        return page.url();
    }
}
