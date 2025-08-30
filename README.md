Playwright Java Automation Framework (POM + PageActions + Factory)

Overview
•	This is a Playwright Java Test Automation Framework built with TestNG, following the Page Object Model (POM) design pattern.
•	It provides reusable utilities, structured reporting with ExtentReports, and support for data-driven testing via Excel.

🔹 Features
✅ Built with Playwright Java for fast & reliable browser automation
✅ TestNG for test management (parallel execution, retries, data providers)
✅ Page Object Model (POM) for maintainability
✅ ExtentReports for beautiful HTML reports with screenshots
✅ Excel Data-driven testing for flexibility
✅ Retry Analyzer for handling flaky tests
✅ Supports multiple browsers: Chrome, Edge, Firefox, WebKit

🔹 Project Structure
src
 ├── main
 │   └── java
 │       ├── base/        # BasePage, BaseTest, PlaywrightFactory
 │       ├── pages/       # Page Objects (LoginPage, HomePage, etc.)
 │       └── utils/       # Utilities (PageActions, ReportManager, ExcelUtils, ScreenshotUtil)
 └── test
     └── java
         └── tests/       # TestNG test classes

🔹 Setup
1. Prerequisites
•	Install Java 17+
•	Install Maven 3.9+
•	Install Node.js (needed for Playwright binaries)
•	Install Playwright browsers:
•	npx playwright install


🔹 Configuration
Edit src/test/resources/config.properties to update settings:
browser=chrome        # chrome | msedge | firefox | webkit
headless=false
maximize.window=true
base.url=https://www.saucedemo.com

🔹 Reports
After execution, open the report:
reports/ExtentReport.html
Report includes:
•	Test results (Pass/Fail/Skip)
•	Screenshots for each step
•	Author info (Venkataraghavan or per @Author annotation)

How to use:
1. Ensure JDK 17+ and Maven installed.
2. From project root run:
   mvn clean test
3. Extent report will be generated under 'reports' directory.

Notes:
- Update selectors in page objects to suit your application.
- Allure dependency is included; to serve Allure you will need Allure CLI installed if you want to use it.

🔹 Author
👨‍💻 Venkataraghavan
Senior Automation Engineer | Playwright + Java | TestNG | ExtentReports
________________________________________
🔹 License
© 2025 Perficient. All rights reserved.


