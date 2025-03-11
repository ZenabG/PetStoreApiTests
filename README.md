# PetStoreAPITests

This project contains automated tests for the Pet Store API using TestNG and Rest Assured.

## Prerequisites

- Java 17
- Maven
- IntelliJ IDEA (or any other IDE)

## Project Structure

- `src/main/java`: Contains the main Java source files.
    - `httpConnection`: Contains classes for setting up HTTP connections.
- `src/test/java`: Contains the test Java source files.
    - `petStoreSwaggerAPITests`: Contains the test classes for the Pet Store API.
- `pom.xml`: Maven configuration file.

## Tools and technologies used

The project uses the following dependencies :
    
- TestNG
- Rest Assured
- Log4j
- JSON Simple
- ExtentReports
- Lombok

These dependencies are defined in the `pom.xml` file.

## Running the Tests

1. Ensure that the `testng.xml` file is present in the root directory of the project.
2. Open a terminal and navigate to the project directory.
3. Run the following command to execute the tests:

   ```sh
   mvn test