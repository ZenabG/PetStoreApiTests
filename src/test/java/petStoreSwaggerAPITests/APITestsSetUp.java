package petStoreSwaggerAPITests;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter; // Updated import
import com.aventstack.extentreports.reporter.configuration.Theme;

public class APITestsSetUp {

	protected static ExtentSparkReporter sparkReporter; // Updated class
	protected static ExtentReports extent;
	protected static ExtentTest test;

	private static final String EXTENT_REPORT_PATH = "./test-extent/extent";

	@BeforeTest
	public void initialiseLog4j() {
		BasicConfigurator.configure();
	}

	@BeforeSuite
	public static void beforeSuite() {
		// Extent report logic
		sparkReporter = new ExtentSparkReporter(EXTENT_REPORT_PATH + System.currentTimeMillis() + ".html");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
	}

	/**
	 * @param ctx This method sets the extent report name, title, and its theme.
	 */
	@BeforeTest
	public void beforeTest(ITestContext ctx) {
		// Extent report logic
		sparkReporter.config().setReportName("Test Report" + ctx.getCurrentXmlTest().getSuite().getName());
		sparkReporter.config().setDocumentTitle("Test Report");
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

		if (result.getStatus() == ITestResult.SKIP)
			test.skip(result.getThrowable().getMessage());

		extent.flush();
	}

	// HTTP Request Methods

	/**
	 * Sends a GET request to the specified URL and returns the response as a string.
	 *
	 * @param urlString The URL to send the GET request to.
	 * @return The response body as a string.
	 * @throws IOException If an I/O error occurs.
	 */
	protected String pullRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return new String(connection.getInputStream().readAllBytes(), "utf-8");
		} else {
			throw new IOException("GET request failed with response code: " + responseCode);
		}
	}

	/**
	 * Sends a POST request to the specified URL with the provided JSON body and returns the response as a string.
	 *
	 * @param urlString      The URL to send the POST request to.
	 * @param jsonInputString The JSON body to send with the request.
	 * @return The response body as a string.
	 * @throws IOException If an I/O error occurs.
	 */
	protected String postRequest(String urlString, String jsonInputString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return new String(connection.getInputStream().readAllBytes(), "utf-8");
		} else {
			throw new IOException("POST request failed with response code: " + responseCode);
		}
	}

	/**
	 * Sends a PUT request to the specified URL with the provided JSON body and returns the response as a string.
	 *
	 * @param urlString      The URL to send the PUT request to.
	 * @param jsonInputString The JSON body to send with the request.
	 * @return The response body as a string.
	 * @throws IOException If an I/O error occurs.
	 */
	protected String putRequest(String urlString, String jsonInputString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return new String(connection.getInputStream().readAllBytes(), "utf-8");
		} else {
			throw new IOException("PUT request failed with response code: " + responseCode);
		}
	}

	/**
	 * Sends a DELETE request to the specified URL and returns the response as a string.
	 *
	 * @param urlString The URL to send the DELETE request to.
	 * @return The response body as a string.
	 * @throws IOException If an I/O error occurs.
	 */
	protected String deleteRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			return new String(connection.getInputStream().readAllBytes(), "utf-8");
		} else {
			throw new IOException("DELETE request failed with response code: " + responseCode);
		}
	}
}