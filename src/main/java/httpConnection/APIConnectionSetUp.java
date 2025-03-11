
package httpConnection;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

public class APIConnectionSetUp {

	private final Logger log = Logger.getLogger(this.getClass());

	private static final String DEADLOCK = "Deadlock";
	private static final int RETRY_MAX = 3;
	private static final int WAIT_MAX = 5000;
	private static final int WAIT_MIN = 1000;

	public String pullRequest(String requestUrl) throws InterruptedException {
		return sendRequestWithRetries("GET", requestUrl, null);
	}

	public String postRequest(String requestUrl, String jsonInputString) throws InterruptedException {
		return sendRequestWithRetries("POST", requestUrl, jsonInputString);
	}

	public String putRequest(String requestUrl, String jsonInputString) throws InterruptedException {
		return sendRequestWithRetries("PUT", requestUrl, jsonInputString);
	}

	public String deleteRequest(String requestUrl) throws InterruptedException {
		return sendRequestWithRetries("DELETE", requestUrl, null);
	}

	private String sendRequestWithRetries(String method, String requestUrl, String jsonInput) throws InterruptedException {
		String response = null;

		for (int i = 0; i < RETRY_MAX; i++) {
			try {
				log.debug(String.format("Making %s request to: %s", method, requestUrl));

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
					log.debug(String.format("Response received: %s", response));
					return response;
				} else {
					throw new RuntimeException(method + " request failed. Status Code: " + statusCode);
				}

			} catch (Exception e) {
				if (e.getMessage().contains(DEADLOCK)) {
					long retryWait = WAIT_MIN + (long) (Math.random() * WAIT_MAX);
					log.error(String.format("%s request: %s, retry: %d. Retrying in %d ms.", method, requestUrl, i, retryWait), e);
					Thread.sleep(retryWait);
				} else {
					throw e;
				}
			}
		}
		return response;
	}
}