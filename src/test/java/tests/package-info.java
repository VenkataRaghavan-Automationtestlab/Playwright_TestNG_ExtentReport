/**
 * Tests package for the Playwright Automation Framework.
 * <p>
 * Contains TestNG test classes that validate application functionality.
 * Tests are written using the Page Object Model (POM) pattern.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Define test scenarios for functional, regression, and smoke testing.</li>
 *   <li>Consume Page Objects from the {@link pages} package.</li>
 *   <li>Leverage {@link utils.ReportManager} for reporting and {@link utils.ExcelUtils} for data-driven tests.</li>
 *   <li>Extend {@link base.BaseTest} for consistent setup/teardown.</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * public class LoginTest extends BaseTest {
 * 
 *     @Test
 *     public void validLoginTest() {
 *         LoginPage login = getPage(LoginPage.class);
 *         login.open(ConfigManager.get("base.url"));
 *         login.login("standard_user", "secret_sauce");
 *
 *         HomePage home = getPage(HomePage.class);
 *         Assert.assertEquals(home.getTitle(), "Products");
 *     }
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package tests;
