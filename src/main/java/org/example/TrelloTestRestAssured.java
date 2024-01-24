package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigReader;

import static io.restassured.RestAssured.given;


/**
 * This class contains TestNG test methods using RestAssured to test various Trello API functionalities.
 * The test scenarios cover actions such as creating Trello boards, lists, and cards, updating cards, and deleting boards and cards.
 * Utilizing TestNG annotations for test execution and the RestAssured library for API requests, the class emphasizes flexibility
 * through dynamic variable handling using the TestNG `ISuite` object.

 * The `ISuite` object allows the dynamic passing of parameters from the suite XML file to the test class, facilitating reusable
 * and configurable test scenarios.

 * When a test starts, it begins by creating a Trello board using an API request. Subsequent actions include creating lists,
 * adding cards to the lists, updating card details randomly, and finally, deleting the created boards and cards. Each test method
 * in this class is designed to execute a specific step in this sequence, and dependencies between methods ensure a logical order
 * of execution.
 */
public class TrelloTestRestAssured {

    private static final String API_TOKEN = ConfigReader.getProperty("APIToken");
    private static final String API_KEY = ConfigReader.getProperty("APIKey");
    private static final String BOARD_ENDPOINT = "/boards";
    private static final String LISTS_ENDPOINT = "/lists";
    private static final String CARDS_ENDPOINT = "/cards";

    /**
     * Setup method to initialize the base URI for RestAssured and set up common test attributes.
     */
    @BeforeClass
    public static void setup(ITestContext context) {
        // Set up the base URI for RestAssured using the Trello base URL from configuration
        RestAssured.baseURI = ConfigReader.getProperty("TrelloBaseUrl");

        // Access the test suite to store and share common attributes across test methods
        ISuite suite = context.getSuite();

        // Set test attributes for reuse in different test methods
        suite.setAttribute("listName", "Trello List");
        suite.setAttribute("cardName2", "Trello Card2");
        suite.setAttribute("boardName", "Trello Board");
        suite.setAttribute("cardName1", "Trello Card1");
    }

    /**
     * Test method to create a Trello board by sending a request to the Trello API with the specified board name.
     * The resulting board ID is stored in test suite attributes for potential future use in subsequent test methods.
     */
    @Test(priority = 1)
    public void createTrelloBoardAndStoreId(ITestContext context) {
        // Retrieve board name from suite attributes
        ISuite suite = context.getSuite();
        String boardName = (String) suite.getAttribute("boardName");

        // Create a Trello board and store the board ID in suite attributes
        String boardId = given()
                .contentType(ContentType.JSON)
                .queryParams("name", boardName, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(BOARD_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created board ID in suite attributes for potential future use
        suite.setAttribute("boardId", boardId);
    }

    /**
     * Test method to create a Trello list on a previously created board.
     * Requires the "createTrelloBoard" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "createTrelloBoardAndStoreId")
    public void createTrelloListOnBoardAndStoreId(ITestContext context) {
        // Retrieve list name and board ID from suite attributes
        ISuite suite = context.getSuite();
        String listName = (String) suite.getAttribute("listName");
        String boardId = (String) suite.getAttribute("boardId");

        // Create a Trello list on the specified board and store the list ID in suite attributes
        String listId = given()
                .contentType(ContentType.JSON)
                .queryParams("name", listName, "idBoard", boardId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(LISTS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created list ID in suite attributes for potential future use
        suite.setAttribute("listId", listId);
    }

    /**
     * Test method to create a Trello Card 1 on a previously created list.
     * Requires the "createTrelloListOnBoard" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "createTrelloListOnBoardAndStoreId")
    public void createTrelloCard1OnListAndStoreId(ITestContext context) {
        // Retrieve card name and list ID from suite attributes
        ISuite suite = context.getSuite();
        String cardName1 = (String) suite.getAttribute("cardName1");
        String listId = (String) suite.getAttribute("listId");

        // Create a Trello Card 1 on the specified list and store the card ID in suite attributes
        String cardId1 = given()
                .contentType(ContentType.JSON)
                .queryParams("name", cardName1, "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(CARDS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created Card 1 ID in suite attributes for potential future use
        suite.setAttribute("cardId1", cardId1);
    }

    /**
     * Test method to create a Trello Card 2 on a previously created list.
     * Requires the "createTrelloCard1OnList" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "createTrelloCard1OnListAndStoreId")
    public void createTrelloCard2OnListAndStoreId (ITestContext context) {
        // Retrieve card name and list ID from suite attributes
        ISuite suite = context.getSuite();
        String cardName2 = (String) suite.getAttribute("cardName2");
        String listId = (String) suite.getAttribute("listId");

        // Create a Trello Card 2 on the specified list and store the card ID in suite attributes
        String cardId2 = given()
                .contentType(ContentType.JSON)
                .queryParams("name", cardName2, "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(CARDS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created Card 2 ID in suite attributes for potential future use
        suite.setAttribute("cardId2", cardId2);
    }

    /**
     * Test method to update a Trello Card on a previously created list randomly.
     * Requires the "createTrelloCard2OnList" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "createTrelloCard2OnListAndStoreId")
    public void updateTrelloCardOnListRandomly(ITestContext context) {
        ISuite suite = context.getSuite();
        // Retrieve random card ID, list ID, and create an update request with new details
        int tmp = (int) (Math.random() * 1) + 1;
        String randomCardID = tmp == 1 ? (String) suite.getAttribute("cardId1") : (String) suite.getAttribute("cardId2");
        String listId = (String) suite.getAttribute("listId");

        given()
                .contentType(ContentType.JSON)
                .queryParams("id", randomCardID, "name", "Trello Card Updated", "color", "blue", "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .put(CARDS_ENDPOINT + "/" + randomCardID)
                .then()
                .statusCode(200);
    }

    /**
     * Test method to delete a Trello Card on a previously created list.
     * Requires the "updateTrelloCardOnListRandomly" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "updateTrelloCardOnListRandomly")
    public void deleteTrelloCardOnList(ITestContext context) {
        // Retrieve Card 1 ID from suite attributes and send a request to delete the card
        ISuite suite = context.getSuite();
        String cardId1 = (String) suite.getAttribute("cardId1");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(CARDS_ENDPOINT + "/" + cardId1)
                .then()
                .statusCode(200);
    }

    /**
     * Test method to delete Trello Card 2 on a previously created list.
     * Requires the "deleteTrelloCardOnList" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "deleteTrelloCardOnList")
    public void deleteTrelloCard2OnList(ITestContext context) {
        // Retrieve Card 2 ID from suite attributes and send a request to delete the card
        ISuite suite = context.getSuite();
        String cardId2 = (String) suite.getAttribute("cardId2");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(CARDS_ENDPOINT + "/" + cardId2)
                .then()
                .statusCode(200);
    }

    /**
     * Test method to delete a Trello Board.
     * Requires the "deleteTrelloCard2OnList" method to be executed first (dependency).
     */
    @Test(dependsOnMethods = "deleteTrelloCard2OnList")
    public void deleteTrelloBoardAndVerifyDeletion (ITestContext context) {
        // Retrieve Board ID from suite attributes and send a request to delete the board
        ISuite suite = context.getSuite();
        String boardId = (String) suite.getAttribute("boardId");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(BOARD_ENDPOINT + "/" + boardId)
                .then()
                .statusCode(200);
    }
}
