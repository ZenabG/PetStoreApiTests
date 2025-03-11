package petStoreSwaggerAPITests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import httpConnection.APIConnectionSetUp;
import org.testng.Assert;
import org.testng.annotations.Test;

import Utils.Constants;
import Utils.JsonUtility;

/**
 * This class contains test methods for Pet Store API.
 */
public class TestAPI extends APIConnectionSetUp {

	List<Long> petIDs = new ArrayList<>();
	JsonUtility jsonUtility = new JsonUtility();

	/**
	 * Test to get all available pet details.
	 *
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 0)
	public void testGetAllAvailablePetDetails() throws InterruptedException {
		System.out.println("Starting test: Get all available pet details");
		String response = pullRequest(Constants.PetStoreAPI + Constants.GetPetsByStatus + Constants.AvailablePetStatus);
		Assert.assertFalse(response.equals("[]") || response.isEmpty());

		Map<Long, String> petIDNameMap = jsonUtility.getPetIDAndNameFromResponseJson(response);

		for (Map.Entry<Long, String> petDetail : petIDNameMap.entrySet()) {
			petIDs.add(petDetail.getKey());
			System.out.println(petDetail.getKey() + " : " + petDetail.getValue());
		}
		System.out.println("Completed test: Get all available pet details");
	}

	/**
	 * Test to find a pet by its ID.
	 *
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 1)
	public void testFindPetByID() throws InterruptedException {
		System.out.println("Starting test: Find pet by ID");
		String response = pullRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIDs.get(0));
		Assert.assertFalse(response.equals("[]") || response.isEmpty());
		System.out.println("Completed test: Find pet by ID");
	}

	/**
	 * Test to create a new pet.
	 *
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 2)
	public void testCreatePet() throws InterruptedException {
		System.out.println("Starting test: Create a new pet");
		String petJson = "{\"id\": 12345, \"name\": \"doggie\", \"status\": \"available\"}";
		String response = postRequest(Constants.PetStoreAPI + Constants.PetEndpoint, petJson);
		Assert.assertFalse(response.isEmpty());

		// Extract the ID of the created pet and add it to the list
		Long createdPetId = jsonUtility.getPetIdFromResponseJson(response);
		petIDs.add(createdPetId);
		System.out.println("Created Pet ID: " + createdPetId);
		System.out.println("Completed test: Create a new pet");
	}

	/**
	 * Test to update an existing pet.
	 *
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test(priority = 3)
	public void testUpdatePet() throws InterruptedException {
		System.out.println("Starting test: Update an existing pet");
		Long petIdToUpdate = petIDs.get(0);
		String updatedPetJson = "{\"id\": " + petIdToUpdate + ", \"name\": \"updatedDoggie\", \"status\": \"sold\"}";
		String response = putRequest(Constants.PetStoreAPI + Constants.PetEndpoint, updatedPetJson);
		Assert.assertFalse(response.isEmpty());
		Assert.assertTrue(response.contains("updatedDoggie"));
		System.out.println("Completed test: Update an existing pet");
	}

	/**
	 * Test to delete a pet by its ID.
	 *
	 * @throws InterruptedException
	 */
	@Test(priority = 4)
	public void testDeletePet() throws InterruptedException {
		System.out.println("Starting test: Delete a pet by ID");
		Long petIdToDelete = petIDs.get(0);
		String response = deleteRequest(Constants.PetStoreAPI + Constants.PetIdStatus + petIdToDelete);
		Assert.assertTrue(response.contains("200"));
		System.out.println("Completed test: Delete a pet by ID");
	}
}