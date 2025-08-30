/**
 * Playwright Framework - TestRetryAnalyzer
 * <p>
 * Custom TestNG {@link org.testng.IRetryAnalyzer} implementation
 * for retrying failed tests automatically.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Retries a failed test a maximum number of times (default: 1).</li>
 *   <li>Useful for handling flaky tests due to network/UI synchronization issues.</li>
 *   <li>Configured at test level using {@code @Test(retryAnalyzer = TestRetryAnalyzer.class)}.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * @Test(retryAnalyzer = utils.TestRetryAnalyzer.class)
 * public void testLogin() {
 *     // Test code here
 * }
 * }</pre>
 *
 * <p><b>Author:</b> Venkataraghavan</p>
 * <p><b>Copyright:</b> © 2025 Perficient</p>
 */
package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Implements a retry mechanism for failed TestNG tests.
 * <p>
 * By default, retries each failed test once (maxRetry = 1).
 * </p>
 */
public class TestRetryAnalyzer implements IRetryAnalyzer {
    /** Current retry attempt count. */
    private int retryCount = 0;

    /** Maximum number of retries allowed. Default is 1. */
    private final int maxRetry;
    /**
     * Determines whether a failed test should be retried.
     *
     * @param result the test result from TestNG
     * @return true if the test should be retried, false otherwise
     */
    public TestRetryAnalyzer() {
        // Read from config.properties, default = 1
        this.maxRetry = Integer.parseInt(ConfigManager.get("retry.count"));
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }
}
