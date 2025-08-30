/**
 * Playwright Framework
 * <p>
 * Base TestNG class for all test classes in the framework.
 * Manages the Playwright browser lifecycle, test initialization,
 * reporting, and teardown using TestNG annotations.
 * </p>
 * Copyright @ 2025 Perficient
 * @author Venkataraghavan
 */

package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.PageActions;
import utils.ReportManager;
import utils.ScreenshotUtil;

/**
 * BaseTest sets up and tears down Playwright context and reporting
 * for each test execution.
 * <p>
 * Extends this class in all test classes to automatically inherit
 * browser/session management and logging capabilities.
 * </p>
 */

public class BaseTest {
	/** Playwright browser instance shared across tests in the class. */
    protected Browser browser;
    /** Browser context created before each test. */
    protected BrowserContext context;
    /** Page instance for interacting with the current test page. */
    protected Page page;
    /** Wrapper around Playwright actions with logging and screenshots. */
    protected PageActions actions;
    /** Stores the current test method name, used for reporting. */
    protected String currentTestName; // 🔹 store the running test name

    
    /**
     * Initializes ExtentReports and starts Playwright before the test class runs.
     */
    @BeforeClass
    public void beforeClass() {
        ReportManager.initReports();
        PlaywrightFactory.start();
        browser = PlaywrightFactory.getBrowser();
    }

    /**
     * Creates a new browser context and page before each test method.
     * Also initializes {@link PageActions} and sets the current test name.
     *
     * @param method the currently running test method (provided by TestNG)
     */
    @BeforeMethod
    public void beforeMethod(java.lang.reflect.Method method) {
        context = PlaywrightFactory.getContext();
        page = context.newPage();

        // 🔹 capture method name automatically
        currentTestName = method.getName();
        actions = new PageActions(page, currentTestName);

        ReportManager.createTest(currentTestName);
    }

    /**
     * Handles test result reporting after each test method.
     * Logs pass, fail, or skip status with optional screenshot on failure.
     *
     * @param result the result of the test method execution
     */
    @AfterMethod
    public void afterMethod(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = ScreenshotUtil.takeScreenshot(page, result.getName() + "_failure");
                ReportManager.logFailWithScreenshot("Test failed: " + result.getName(), screenshotPath);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ReportManager.logPass("Test passed: " + result.getName());
            } else if (result.getStatus() == ITestResult.SKIP) {
                ReportManager.logSkip("Test skipped: " + result.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (context != null) {
                context.close();
                context = null;
            }
            page = null;
            actions = null;
        }
    }
    
    
    /**
     * Stops Playwright and flushes reports after all tests in the class.
     */
    @AfterClass
    public void afterClass() {
        PlaywrightFactory.stop();
        ReportManager.flush();
    }

    /**
     * Generic Page Object factory method.
     * Creates and returns an instance of the given Page Object class,
     * injecting the Playwright {@link Page} and current test name.
     *
     * @param pageClass the Page Object class type to create
     * @param <T>       the type extending {@link base.BasePage}
     * @return an instance of the requested Page Object
     */
    // 🔹 Page Factory helper
    protected <T extends BasePage> T getPage(Class<T> pageClass) {
        try {
            return pageClass.getConstructor(Page.class, String.class)
                    .newInstance(page, currentTestName); // ✅ uses stored test name
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page: " + pageClass.getSimpleName(), e);
        }
    }
}
