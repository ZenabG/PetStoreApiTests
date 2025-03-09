package Utils;

public class Constants {
	// Base URL for the Pet Store API
	public static final String PetStoreAPI = "https://petstore.swagger.io/v2";

	// Endpoints
	public static final String PetEndpoint = "/pet"; // Endpoint for pet operations
	public static final String GetPetsByStatus = "/pet/findByStatus?status="; // Endpoint to get pets by status
	public static final String PetIdStatus = "/pet/"; // Endpoint to get pet by ID

	// Status values
	public static final String AvailablePetStatus = "available"; // Status for available pets
	public static final String SoldPetStatus = "sold"; // Status for sold pets
	public static final String PendingPetStatus = "pending"; // Status for pending pets
}