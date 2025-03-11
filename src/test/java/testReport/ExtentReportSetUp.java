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

public class ExtentReportSetUp {

	protected static ExtentSparkReporter sparkReporter;
	protected static ExtentReports extent;
	protected static ExtentTest test;

	@BeforeTest
	public void initialiseLog4j() {
		BasicConfigurator.configure();
	}

	@BeforeSuite
	public static void beforeSuite() {
		// Extent report logic
		sparkReporter = new ExtentSparkReporter("extent_report.html");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
	}

	/**
	 * @param ctx This method sets the extent report name, title, and its theme.
	 */
	@BeforeTest
	public void beforeTest(ITestContext ctx) {
		// Extent report logic
		sparkReporter.config().setReportName("Petstore API Tests Report");
		sparkReporter.config().setDocumentTitle("Petstore API Tests Report");
		sparkReporter.config().setTheme(Theme.STANDARD);
	}

	@BeforeMethod
	public static void before(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
	}

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