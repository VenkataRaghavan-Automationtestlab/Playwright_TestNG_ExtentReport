/**
 * Base package for the Playwright Automation Framework.
 * <p>
 * Contains the foundational classes required to build Page Objects and manage 
 * browser lifecycle within the Playwright-based automation framework.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Provides {@link base.BasePage} — the root class for all Page Objects.</li>
 *   <li>Encapsulates Playwright's {@link com.microsoft.playwright.Page} object.</li>
 *   <li>Initializes reusable {@link utils.PageActions} for logging and screenshots.</li>
 *   <li>Ensures consistency and reusability across the framework.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * public class LoginPage extends BasePage {
 *     private final String usernameInput = "#username";
 *     private final String passwordInput = "#password";
 *     private final String loginButton   = "#login-button";
 *
 *     public LoginPage(Page page, String testName) {
 *         super(page, testName);
 *     }
 *
 *     public void login(String username, String password) {
 *         actions.fill(usernameInput, username);
 *         actions.fill(passwordInput, password);
 *         actions.click(loginButton);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package base;