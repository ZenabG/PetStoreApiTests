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
		String response = null;

		for (int i = 0; i < RETRY_MAX; i++) {
			try {
				log.debug(String.format("Making request to: %s", requestUrl));

				Response res = RestAssured
						.given()
						.relaxedHTTPSValidation() // Bypass SSL issues if needed
						.get(requestUrl);

				int statusCode = res.getStatusCode();
				response = res.getBody().asString();

				if (statusCode == 200) {
					log.debug(String.format("Response received: %s", response));
					return response;
				} else {
					throw new RuntimeException("Connection couldn't be established. Status Code: " + statusCode);
				}

			} catch (Exception e) {
				if (e.getMessage().contains(DEADLOCK)) { // Retry on deadlock errors
					long retryWait = WAIT_MIN + (long) (Math.random() * WAIT_MAX);
					log.error(String.format("Pull request: %s, retry: %d. Retrying in %d ms.", requestUrl, i, retryWait), e);
					Thread.sleep(retryWait);
				} else {
					throw e;
				}
			}
		}
		return response;
	}
}