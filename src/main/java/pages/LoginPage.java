package pages;

import com.microsoft.playwright.Page;
import base.BasePage;

public class LoginPage extends BasePage {
    // Locators
    private final String usernameInput = "#user-name";
    private final String passwordInput = "#password";
    private final String loginButton   = "#login-button";
    private final String errorMsg      = "#error-message";

    public LoginPage(Page page, String testName) {
        super(page, testName);
    }

    // Actions
    public LoginPage open(String url) {
        actions.navigate(url);
		return this;
    }

    public LoginPage login(String username, String password) {
        actions.fill(usernameInput, username);
        actions.fill(passwordInput, password);
        actions.click(loginButton);
		return this;
    }

    public String getErrorMessage() {
        return actions.getText(errorMsg);
    }
}
