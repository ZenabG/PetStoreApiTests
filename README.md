# PetStoreAPITests

## Overview

The Pet Store API is a sample REST API that provides endpoints for managing pets, users, and orders. The API is publicly available and can be accessed at [https://petstore3.swagger.io/](https://petstore.swagger.io/v2).

The project is created using Java with TestNG and Rest Assured frameworks. The project is designed to test the functionality of the Pet Store API.

## Tools and technologies used

- **Java** : Programming language used for writing the test scripts
- **TestNG** : Testing framework used for executing the tests
- **Rest Assured** : Java library used for testing and validating REST services
- **Maven** : Build automation tool used for managing dependencies and building the project
- **JSON Simple** : Java library used for parsing JSON data
- **ExtentReports** : Java library used for generating test reports

## Project Structure

- `src/main/java`: Contains the main Java source files.
    - `httpConnection`: Contains classes for setting up HTTP connections using RestAssured framework.
- `src/test/java`: Contains the test Java source files.
    - `petStoreSwaggerAPITests`: Contains the test classes for the Pet Store API.
    - `testReport`: Contains classes for setting up test reports using ExtentReports.
- `pom.xml`: Maven configuration file.

## Test Reports
- ExtentReports is used to generate test reports.
- The reports are generated in the `test-extent` directory with the name `report.html`.

## Functional Tests

The following functional tests are included in the project:

1. **testGetAllAvailablePetDetails**: This test retrieves all available pet details from the Pet Store API and verifies that the response is not empty. It also extracts and prints the pet IDs and names.

2. **testFindPetByID**: This test retrieves the details of a specific pet by its ID and verifies that the response is not empty.

3. **testCreatePet**: This test creates a new pet with a specified ID, name, and status. It verifies that the response is not empty and extracts the ID of the created pet.

4. **testUpdatePet**: This test updates the details of an existing pet by its ID. It verifies that the response is not empty and contains the updated pet name.

5. **testDeletePet**: This test deletes a specific pet by its ID and verifies that the response contains a status code of 200.

## How to run tests locally
### Pre-requisite :
* Java 17 installed and JAVA_HOME set in environment variable
* Maven installed and MAVEN_HOME set in environment variable
* An IDE (Eclipse or IntelliJ IDEA)
* Git installed

### Steps to download the project in IDE:
1. Open command prompt and go to the path where the project is to be downloaded
2. Run `git clone <url>`. Get the URL from the project path
3. Open IDE and go to File > Open and choose the git cloned project.

### Steps to run the tests locally using Maven:
1. Run the command `mvn clean test` in the root directory of the project to run the tests.
2. The test results are displayed in the console and the custom test reports are generated in the root directory with name `extent_report.html`.

## How to run tests using CI/CD
* The project is integrated with Github Actions for CI/CD.
* The workflow file is present in the `.github/workflows` directory.
* The workflow file is configured to run the tests on every push and pull request to the `master` branch.