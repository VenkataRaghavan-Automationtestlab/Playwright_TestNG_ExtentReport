/**
 * Pages package for the Playwright Automation Framework.
 * <p>
 * Implements the Page Object Model (POM) pattern where
 * each application page is represented by a dedicated Java class.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Encapsulate locators and page-specific actions.</li>
 *   <li>Use {@link utils.PageActions} for reusable Playwright interactions.</li>
 *   <li>Provide high-level business workflows for tests.</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * public class LoginPage extends BasePage {
 *     private final String username = "#username";
 *     private final String password = "#password";
 *     private final String loginBtn = "#login-button";
 *
 *     public LoginPage(Page page, String testName) {
 *         super(page, testName);
 *     }
 *
 *     public void login(String user, String pass) {
 *         actions.fill(username, user);
 *         actions.fill(password, pass);
 *         actions.click(loginBtn);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package pages;
