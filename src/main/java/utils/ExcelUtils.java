/**
 * Playwright Framework - ExcelUtils
 * <p>
 * Utility class for reading Excel test data files using Apache POI.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Reads an Excel sheet into a list of String arrays.</li>
 *   <li>Supports data-driven testing with TestNG {@code @DataProvider}.</li>
 *   <li>Skips header row automatically.</li>
 *   <li>Safely converts cell values of types: STRING, NUMERIC, BOOLEAN, FORMULA, BLANK.</li>
 * </ul>
 *
 * <p><b>Usage Example with TestNG:</b></p>
 * <pre>{@code
 * @DataProvider(name = "loginData")
 * public Object[][] loginDataProvider() {
 *     List<String[]> dataList = ExcelUtils.readSheet("src/test/resources/testdata.xlsx", "Sheet1");
 *     return dataList.toArray(new Object[0][]);
 * }
 *
 * @Test(dataProvider = "loginData")
 * public void testLogin(String username, String password) {
 *     System.out.println("User: " + username + ", Pass: " + password);
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling Excel data.
 * <p>
 * Provides a static method {@link #readSheet(String, String)} to read
 * all rows from an Excel sheet (skipping the header row).
 * </p>
 */
public class ExcelUtils {

    /**
     * Reads an Excel sheet and converts its rows into a {@link List} of {@code String[]} arrays.
     * <p>
     * - Skips the header row (first row). <br>
     * - Handles cell types: STRING, NUMERIC, BOOLEAN, FORMULA, BLANK. <br>
     * - Converts all values into Strings for easy use in tests.
     * </p>
     *
     * @param path      the path to the Excel file (.xlsx)
     * @param sheetName the sheet name to read
     * @return a list of String arrays, where each array represents one row of data
     * @throws RuntimeException if the file or sheet cannot be read
     */
    public static List<String[]> readSheet(String path, String sheetName) {
        List<String[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) return rows;

            int firstRowNum = sheet.getFirstRowNum() + 1; // skip header
            int lastRowNum = sheet.getLastRowNum();

            for (int i = firstRowNum; i <= lastRowNum; i++) {
                Row r = sheet.getRow(i);
                if (r == null) continue;

                List<String> cells = new ArrayList<>();
                for (int j = 0; j < r.getLastCellNum(); j++) {
                    Cell c = r.getCell(j);
                    if (c == null) {
                        cells.add("");
                        continue;
                    }

                    // Convert cell to string safely
                    switch (c.getCellType()) {
                        case STRING -> cells.add(c.getStringCellValue());
                        case NUMERIC -> cells.add(String.valueOf((int) c.getNumericCellValue()));
                        case BOOLEAN -> cells.add(String.valueOf(c.getBooleanCellValue()));
                        case FORMULA -> cells.add(c.getCellFormula());
                        case BLANK, _NONE, ERROR -> cells.add("");
                        default -> cells.add(c.toString());
                    }
                }
                rows.add(cells.toArray(new String[0]));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel sheet: " + e.getMessage(), e);
        }

        return rows;
    }
}
