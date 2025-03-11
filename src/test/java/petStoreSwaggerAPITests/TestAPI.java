package petStoreSwaggerAPITests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import testReport.ExtentReportSetUp;
import httpConnection.APIConnectionSetUp;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import Utils.Constants;
import Utils.JsonUtility;

/**
 * This class contains test methods for Pet Store API.
 */
public class TestAPI extends ExtentReportSetUp {

	List<Long> petIDs = new ArrayList<>();
	JsonUtility jsonUtility = new JsonUtility();

	APIConnectionSetUp apiConnectionSetUp = new APIConnectionSetUp();

	/**
	 * Test to get all available pet details.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 0)
	public void testGetAllAvailablePetDetails() throws IOException, InterruptedException, ParseException {
		String response = apiConnectionSetUp.pullRequest(Constants.PetStoreAPI + Constants.GetPetsByStatus + Constants.AvailablePetStatus);
		Assert.assertFalse(response.equals("[]") || response.isEmpty());

		Map<Long, String> petIDNameMap = jsonUtility.getPetIDAndNameFromResponseJson(response);

		for (Map.Entry<Long, String> petDetail : petIDNameMap.entrySet()) {
			petIDs.add(petDetail.getKey());
			System.out.println(petDetail.getKey() + " : " + petDetail.getValue());
		}
	}

	/**
	 * Test to find a pet by its ID.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 1)
	public void testFindPetByID() throws IOException, InterruptedException, ParseException {
		String response = apiConnectionSetUp.pullRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIDs.get(0));
		Assert.assertFalse(response.equals("[]") || response.isEmpty());
	}

	/**
	 * Test to create a new pet.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 2)
	public void testCreatePet() throws IOException, InterruptedException, ParseException {
		String petJson = "{\"id\": 12345, \"name\": \"doggie\", \"status\": \"available\"}";
		String response = apiConnectionSetUp.postRequest(Constants.PetStoreAPI + Constants.PetEndpoint, petJson);
		Assert.assertFalse(response.isEmpty());

		// Extract the ID of the created pet and add it to the list
		Long createdPetId = jsonUtility.getPetIdFromResponseJson(response);
		petIDs.add(createdPetId);
		System.out.println("Created Pet ID: " + createdPetId);
	}

	/**
	 * Test to update an existing pet.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 3)
	public void testUpdatePet() throws IOException, InterruptedException, ParseException {
		Long petIdToUpdate = petIDs.get(0);
		String updatedPetJson = "{\"id\": " + petIdToUpdate + ", \"name\": \"updatedDoggie\", \"status\": \"sold\"}";
		String response = apiConnectionSetUp.putRequest(Constants.PetStoreAPI + Constants.PetEndpoint, updatedPetJson);
		Assert.assertFalse(response.isEmpty());
		Assert.assertTrue(response.contains("updatedDoggie"));
	}

	/**
	 * Test to delete a pet by its ID.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(priority = 4)
	public void testDeletePet() throws IOException, InterruptedException {
		Long petIdToDelete = petIDs.get(0);
		String response = apiConnectionSetUp.deleteRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIdToDelete);
		Assert.assertTrue(response.contains("200"));
	}
}