package pages;

import com.microsoft.playwright.Page;
import base.BasePage;

public class HomePage extends BasePage {
    private final String productsTitle = ".title";

    public HomePage(Page page, String testName) {
        super(page, testName);
    }

    public String getTitle() {
        return actions.getText(productsTitle);
    }
}
