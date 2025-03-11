package httpConnection;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * This class sets up the API connection and handles HTTP requests with retries.
 */
public class APIConnectionSetUp {

	private static final String DEADLOCK = "Deadlock";
	private static final int RETRY_MAX = 3;
	private static final int WAIT_MAX = 5000;
	private static final int WAIT_MIN = 1000;

	/**
	 * Sends a GET request to the specified URL.
	 *
	 * @param requestUrl the URL to send the GET request to
	 * @return the response body as a String
	 * @throws InterruptedException if the thread is interrupted during retries
	 */
	public String pullRequest(String requestUrl) throws InterruptedException {
		return sendRequestWithRetries("GET", requestUrl, null);
	}

	/**
	 * Sends a POST request to the specified URL with the given JSON input.
	 *
	 * @param requestUrl the URL to send the POST request to
	 * @param jsonInputString the JSON input string to include in the POST request
	 * @return the response body as a String
	 * @throws InterruptedException if the thread is interrupted during retries
	 */
	public String postRequest(String requestUrl, String jsonInputString) throws InterruptedException {
		return sendRequestWithRetries("POST", requestUrl, jsonInputString);
	}

	/**
	 * Sends a PUT request to the specified URL with the given JSON input.
	 *
	 * @param requestUrl the URL to send the PUT request to
	 * @param jsonInputString the JSON input string to include in the PUT request
	 * @return the response body as a String
	 * @throws InterruptedException if the thread is interrupted during retries
	 */
	public String putRequest(String requestUrl, String jsonInputString) throws InterruptedException {
		return sendRequestWithRetries("PUT", requestUrl, jsonInputString);
	}

	/**
	 * Sends a DELETE request to the specified URL.
	 *
	 * @param requestUrl the URL to send the DELETE request to
	 * @return the response body as a String
	 * @throws InterruptedException if the thread is interrupted during retries
	 */
	public String deleteRequest(String requestUrl) throws InterruptedException {
		return sendRequestWithRetries("DELETE", requestUrl, null);
	}

	/**
	 * Sends an HTTP request with retries in case of failures.
	 *
	 * @param method the HTTP method to use (GET, POST, PUT, DELETE)
	 * @param requestUrl the URL to send the request to
	 * @param jsonInput the JSON input string to include in the request (if applicable)
	 * @return the response body as a String
	 * @throws InterruptedException if the thread is interrupted during retries
	 */
	private String sendRequestWithRetries(String method, String requestUrl, String jsonInput) throws InterruptedException {
		String response = null;

		for (int i = 0; i < RETRY_MAX; i++) {
			try {
				System.out.println(String.format("Making %s request to: %s", method, requestUrl));

				Response res = RestAssured.given()
						.relaxedHTTPSValidation()
						.contentType("application/json")
						.accept("application/json")
						.body(jsonInput != null ? jsonInput : "")
						.when()
						.request(method, requestUrl);

				int statusCode = res.getStatusCode();
				response = res.getBody().asString();

				if (statusCode == 200 || statusCode == 201) {
					System.out.println(String.format("Response received: %s", response));
					return response;
				} else {
					throw new RuntimeException(method + " request failed. Status Code: " + statusCode);
				}

			} catch (Exception e) {
				if (e.getMessage().contains(DEADLOCK)) {
					long retryWait = WAIT_MIN + (long) (Math.random() * WAIT_MAX);
					System.err.println(String.format("%s request: %s, retry: %d. Retrying in %d ms.", method, requestUrl, i, retryWait));
					Thread.sleep(retryWait);
				} else {
					throw e;
				}
			}
		}
		return response;
	}
}