package testReport;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * This class sets up the ExtentReports for logging test results.
 */
public class ExtentReportSetUp {

	protected static ExtentSparkReporter sparkReporter;
	protected static ExtentReports extent;
	protected static ExtentTest test;

	/**
	 * Initializes Log4j configuration.
	 */
	@BeforeTest
	public void initialiseLog4j() {
		BasicConfigurator.configure();
	}

	/**
	 * Sets up the ExtentReports before the test suite starts.
	 */
	@BeforeSuite
	public static void beforeSuite() {
		sparkReporter = new ExtentSparkReporter("extent_report.html");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
	}

	/**
	 * Configures the ExtentReports before each test.
	 *
	 * @param ctx the test context
	 */
	@BeforeTest
	public void beforeTest(ITestContext ctx) {
		sparkReporter.config().setReportName("Petstore API Tests Report");
		sparkReporter.config().setDocumentTitle("Petstore API Tests Report");
		sparkReporter.config().setTheme(Theme.STANDARD);
	}

	/**
	 * Creates a new test entry in the ExtentReports before each test method.
	 *
	 * @param result the test result
	 */
	@BeforeMethod
	public static void before(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
	}

	/**
	 * Logs the test result in the ExtentReports after each test method.
	 *
	 * @param result the test result
	 * @throws IOException if an I/O error occurs
	 */
	@AfterMethod
	public void getTestResultInExtentReport(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.fail(result.getThrowable().getMessage());
		}

		if (result.getStatus() == ITestResult.SKIP) {
			test.skip(result.getThrowable().getMessage());
		}

		extent.flush();
	}
}