package httpConnection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class APIConnectionSetUp {

	private final Logger log = Logger.getLogger(this.getClass());

	private static final String DEADLOCK = "Deadlock";
	private static final int RETRY_MAX = 3;
	private static final int RETRY_MIN = 0;
	private static final int WAIT_MAX = 5000;
	private static final int WAIT_MIN = 1000;

	private static final String LINE_TERMINATOR = System.getProperty("line.separator");

	public String pullRequest(String request) throws IOException, InterruptedException {
		
		String response = "";
		
		for (int i = RETRY_MIN; i < RETRY_MAX; i++) {
			try {
				HttpURLConnection conn = setupConnection(request);
				System.out.println(conn.getURL());
				response = readResponse(conn, request);
				
				if(response!=null) {
					break;
				}
				
			} catch (Exception e) {
				if (e.getMessage().contains(DEADLOCK)) {// See if this is due to a deadlock.

					long retryWait = WAIT_MIN + (long) (Math.random() * WAIT_MAX);
					log.error(String.format("Pull request: %s, retry: %d.  Will retry again in: %d ms.", request, i,
							retryWait), e);
					Thread.sleep(retryWait);
				} else {
					throw e;
				}
			}
		}
		return response;
	}

	
	protected HttpURLConnection setupConnection(String request) throws IOException {
		HttpURLConnection conn = null;
		try {
			log.debug(String.format("Setting up connection for %s", request));
			URL url = new URL(request);
			conn = (HttpURLConnection) url.openConnection();
			log.debug(String.format("Connection for %s setup is complete", request));
			
		} catch (IOException io) {
			io.getStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return conn;
	}

	
	private String readResponse(HttpURLConnection conn, String request) throws IOException {
		BufferedReader reader = null;
		String response = null;
		try {
			int status = conn.getResponseCode();
			InputStream iStream;
			if (status != 200) {
				iStream = conn.getErrorStream();
			} else {
				iStream = conn.getInputStream();
			}

			reader = new BufferedReader(new InputStreamReader(iStream));

			response = reader.lines().collect(Collectors.joining(LINE_TERMINATOR));

			if (status != 200) {
				throw new RuntimeException("Connection couldn't be established");
			}

			log.debug(String.format("Response: %s", response));
		} catch (IOException io) {

			throw new IOException();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return response;
	}
}
