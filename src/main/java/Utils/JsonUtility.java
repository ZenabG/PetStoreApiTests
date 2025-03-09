package Utils;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtility {

	private JSONParser parser = new JSONParser();
	private Map<Long, String> petDetails = new HashMap<>();

	/**
	 * Extracts pet IDs and names from a JSON array response.
	 *
	 * @param response The JSON response as a string.
	 * @return A map of pet IDs to pet names.
	 * @throws ParseException If the JSON response cannot be parsed.
	 */
	public Map<Long, String> getPetIDAndNameFromResponseJson(String response) throws ParseException {
		JSONArray jsonArray = (JSONArray) parser.parse(response);

		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;

			if (jsonObj.containsKey("id") && jsonObj.containsKey("name")) {
				petDetails.put(Long.parseLong(jsonObj.get("id").toString()), jsonObj.get("name").toString());
			}
		}

		return petDetails;
	}

	/**
	 * Extracts the pet ID from a single JSON object response.
	 *
	 * @param jsonResponse The JSON response as a string.
	 * @return The pet ID as a Long.
	 * @throws ParseException If the JSON response cannot be parsed.
	 */
	public Long getPetIdFromResponseJson(String jsonResponse) throws ParseException {
		JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
		if (jsonObject.containsKey("id")) {
			return Long.parseLong(jsonObject.get("id").toString());
		} else {
			throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN, "The JSON response does not contain an 'id' field.");
		}
	}
}