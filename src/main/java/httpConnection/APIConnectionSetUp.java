package httpConnection;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

public class APIConnectionSetUp {

	private final Logger log = Logger.getLogger(this.getClass());

	private static final String DEADLOCK = "Deadlock";
	private static final int RETRY_MAX = 3;
	private static final int RETRY_MIN = 0;
	private static final int WAIT_MAX = 5000;
	private static final int WAIT_MIN = 1000;

	public String pullRequest(String request) throws InterruptedException {
		String response = "";

		for (int i = RETRY_MIN; i < RETRY_MAX; i++) {
			try {
				Response res = setupConnection(request);
				response = readResponse(res, request);

				if (response != null) {
					break;
				}

			} catch (Exception e) {
				if (e.getMessage().contains(DEADLOCK)) {
					long retryWait = WAIT_MIN + (long) (Math.random() * WAIT_MAX);
					log.error(String.format("Pull request: %s, retry: %d. Will retry again in: %d ms.", request, i, retryWait), e);
					Thread.sleep(retryWait);
				} else {
					throw e;
				}
			}
		}
		return response;
	}

	protected Response setupConnection(String request) {
		log.debug(String.format("Setting up connection for %s", request));
		Response response = RestAssured.get(request);
		log.debug(String.format("Connection for %s setup is complete", request));
		return response;
	}

	private String readResponse(Response res, String request) {
		int status = res.getStatusCode();
		String response = res.getBody().asString();

		if (status != 200) {
			throw new RuntimeException("Connection couldn't be established");
		}

		log.debug(String.format("Response: %s", response));
		return response;
	}
}