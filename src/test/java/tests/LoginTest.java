/**
 * Playwright Framework - LoginTest
 * <p>
 * Example TestNG test class for validating login functionality
 * using the Playwright + TestNG framework.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Data-driven login tests using Excel data provider.</li>
 *   <li>Retries failed tests once using {@link utils.TestRetryAnalyzer}.</li>
 *   <li>Validates successful login by checking home page title.</li>
 *   <li>Integrates with framework's BaseTest for setup/teardown.</li>
 * </ul>
 *
 * <p><b>Test Data:</b></p>
 * The test reads login credentials from:
 * <pre>{@code
 * src/test/resources/testdata.xlsx
 *   Sheet: Sheet1
 *   Columns: username | password
 * }</pre>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * mvn clean test -Dbrowser=chrome -Dheadless=false
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.HomePage;
import utils.ConfigManager;
import utils.ExcelUtils;

import java.util.List;

/**
 * TestNG test class to validate login functionality.
 */
public class LoginTest extends BaseTest {

    /**
     * Provides login credentials from Excel sheet.
     * <p>
     * Reads from {@code src/test/resources/testdata.xlsx}, sheet {@code Sheet1}.
     * Converts each row into a {@code String[]} (username, password).
     * </p>
     *
     * @return 2D Object array containing login data
     */
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        List<String[]> dataList = ExcelUtils.readSheet("src/test/resources/testdata.xlsx", "Sheet1");
        // Convert List<String[]> to Object[][]
        Object[][] data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        return data;
    }

    /**
     * Validates login functionality with multiple credentials.
     * <p>
     * Steps:
     * <ol>
     *   <li>Opens login page.</li>
     *   <li>Enters username and password from data provider.</li>
     *   <li>Validates successful login by checking Home page title.</li>
     * </ol>
     * </p>
     *
     * <p>Retries failed tests once via {@link utils.TestRetryAnalyzer}.</p>
     *
     * @param username test username from Excel
     * @param password test password from Excel
     */
    @Test(dataProvider = "loginData", retryAnalyzer = utils.TestRetryAnalyzer.class)
    public void validLoginTest(String username, String password) {
        // Page object for login
        LoginPage login = getPage(LoginPage.class); // ✅ currentTestName auto-injected
        login.open(ConfigManager.get("base.url"))
             .login(username, password);

        // Page object for home
        HomePage home = getPage(HomePage.class);

        // Validation
        Assert.assertEquals(home.getTitle(), "Products", "Home page title mismatch!");
    }
}
