package petStoreSwaggerAPITests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import Utils.Constants;
import Utils.JsonUtility;

public class TestAPI extends APITestsSetUp {

	List<Long> petIDs = new ArrayList<>();
	JsonUtility jsonUtility = new JsonUtility();

	@Test(priority = 0)
	public void testGetAllAvailablePetDetails() throws IOException, InterruptedException, ParseException {
		String response = pullRequest(Constants.PetStoreAPI + Constants.GetPetsByStatus + Constants.AvailablePetStatus);
		Assert.assertFalse(response.equals("[]") || response.isEmpty());

		Map<Long, String> petIDNameMap = jsonUtility.getPetIDAndNameFromResponseJson(response);

		for (Map.Entry<Long, String> petDetail : petIDNameMap.entrySet()) {
			petIDs.add(petDetail.getKey());
			System.out.println(petDetail.getKey() + " : " + petDetail.getValue());
		}
	}

	@Test(priority = 1)
	public void testFindPetByID() throws IOException, InterruptedException, ParseException {
		String response = pullRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIDs.get(0));
		Assert.assertFalse(response.equals("[]") || response.isEmpty());
	}

	@Test(priority = 2)
	public void testCreatePet() throws IOException, InterruptedException, ParseException {
		String petJson = "{\"id\": 12345, \"name\": \"doggie\", \"status\": \"available\"}";
		String response = postRequest(Constants.PetStoreAPI + Constants.PetEndpoint, petJson);
		Assert.assertFalse(response.isEmpty());

		// Extract the ID of the created pet and add it to the list
		Long createdPetId = jsonUtility.getPetIdFromResponseJson(response);
		petIDs.add(createdPetId);
		System.out.println("Created Pet ID: " + createdPetId);
	}

	@Test(priority = 3)
	public void testUpdatePet() throws IOException, InterruptedException, ParseException {
		Long petIdToUpdate = petIDs.get(0);
		String updatedPetJson = "{\"id\": " + petIdToUpdate + ", \"name\": \"updatedDoggie\", \"status\": \"sold\"}";
		String response = putRequest(Constants.PetStoreAPI + Constants.PetEndpoint, updatedPetJson);
		Assert.assertFalse(response.isEmpty());

		// Verify the update
		String updatedResponse = pullRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIdToUpdate);
		Assert.assertTrue(updatedResponse.contains("updatedDoggie"));
	}

	@Test(priority = 4)
	public void testDeletePet() throws IOException, InterruptedException {
		Long petIdToDelete = petIDs.get(0);
		String response = deleteRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIdToDelete);
		Assert.assertEquals(response, "200"); // Assuming the API returns an empty response on successful deletion

		// Verify the deletion
		String deletedResponse = pullRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIdToDelete);
		Assert.assertTrue(deletedResponse.contains("Pet not found")); // Assuming the API returns this message for a non-existent pet
	}
}