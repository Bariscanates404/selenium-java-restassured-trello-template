package org.example;

import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import utilities.ConfigReader;

import java.util.HashMap;
import java.util.Map;


/**
 * This class contains TestNG test methods using the Unirest library to interact with the Trello API.
 * The test suite covers the end-to-end process of creating a Trello board, lists, and cards, updating card details,
 * and finally, deleting the boards and cards. Dependencies between methods ensure a sequential execution order.

 * Test methods use configuration properties for Trello board name, list names, API key, and token, allowing flexibility
 * and reusability. The Unirest library facilitates HTTP requests to the Trello API, and dynamic handling of Trello
 * entities' IDs is managed through configuration properties.

 * The random update of a Trello card's name and color adds variability to the test scenarios. After test execution,
 * the class cleans up by deleting the created Trello cards and boards to maintain a clean test environment.

 * When a test starts, it begins by creating a Trello board using an API request. Subsequent actions include creating lists,
 * adding cards to the lists, updating card details randomly, and finally, deleting the created boards and cards. Each test method
 * in this class is designed to execute a specific step in this sequence, and dependencies between methods ensure a logical order
 * of execution.
 */
public class TrelloTestUnitest {

    private static final String BASE_URL = "https://api.trello.com/1";
    private static final String BOARD_ENDPOINT = BASE_URL + "/boards";
    private static final String LISTS_ENDPOINT = BASE_URL + "/lists";
    private static final String CARDS_ENDPOINT = BASE_URL + "/cards";

    /**
     * Creates a new Trello board.
     * Uses the configuration properties for board name, API key, and token.
     * Sets the created board's ID in the configuration for future reference.
     */
    @Test(priority = 1)
    public void createTrelloBoard() throws UnirestException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", ConfigReader.getProperty("boardName"));
        params.put("key", ConfigReader.getProperty("APIKey"));
        params.put("token", ConfigReader.getProperty("APIToken"));

        HttpResponse<String> response = sendPostRequest(BOARD_ENDPOINT, params);
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("boardId", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates a new Trello list on the previously created board.
     * Uses the configuration properties for list name, board ID, API key, and token.
     * Sets the created list's ID in the configuration for future reference.
     */
    @Test(dependsOnMethods = "createTrelloBoard")
    public void createTrelloListOnBoard() throws UnirestException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", ConfigReader.getProperty("listName"));
        params.put("idBoard", ConfigReader.getProperty("boardId"));
        params.put("key", ConfigReader.getProperty("APIKey"));
        params.put("token", ConfigReader.getProperty("APIToken"));

        HttpResponse<String> response = sendPostRequest(LISTS_ENDPOINT, params);
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("listId", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates the first Trello card on the previously created list.
     * Uses the configuration properties for card name, list ID, API key, and token.
     * Sets the created card's ID in the configuration for future reference.
     */
    @Test(dependsOnMethods = "createTrelloListOnBoard")
    public void createTrelloCard1OnList() throws UnirestException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", ConfigReader.getProperty("cardName1"));
        params.put("idList", ConfigReader.getProperty("listId"));
        params.put("key", ConfigReader.getProperty("APIKey"));
        params.put("token", ConfigReader.getProperty("APIToken"));

        HttpResponse<String> response = sendPostRequest(CARDS_ENDPOINT, params);
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("cardId1", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates the second Trello card on the previously created list.
     * Uses the configuration properties for card name, list ID, API key, and token.
     * Sets the created card's ID in the configuration for future reference.
     */
    @Test(dependsOnMethods = "createTrelloCard1OnList")
    public void createTrelloCard2OnList() throws UnirestException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", ConfigReader.getProperty("cardName2"));
        params.put("idList", ConfigReader.getProperty("listId"));
        params.put("key", ConfigReader.getProperty("APIKey"));
        params.put("token", ConfigReader.getProperty("APIToken"));

        HttpResponse<String> response = sendPostRequest(CARDS_ENDPOINT, params);
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("cardId2", jsonResponse.get("id").getAsString());
    }

    /**
     * Updates a randomly selected Trello card's name and color on the previously created list.
     * Uses a random number to choose between the first and second card.
     * Uses the configuration properties for card IDs, list ID, API key, and token.
     */
    @Test(dependsOnMethods = "createTrelloCard2OnList")
    public void updateTrelloCardOnListRandomly() throws UnirestException, InterruptedException {
        String cardID;
        int tmp = (int) (Math.random() * 1) + 1;
        if (tmp == 1) {
            cardID = ConfigReader.getProperty("cardId1");
        } else {
            cardID = ConfigReader.getProperty("cardId2");
        }
        String urlUpdated = CARDS_ENDPOINT + "/" + cardID;

        Map<String, Object> params = new HashMap<>();
        params.put("id", cardID);
        params.put("name", "Trello Card Updated");
        params.put("color", "blue");
        params.put("idList", ConfigReader.getProperty("listId"));
        params.put("key", ConfigReader.getProperty("APIKey"));
        params.put("token", ConfigReader.getProperty("APIToken"));

        sendPutRequest(urlUpdated, params);
    }

    /**
     * Deletes the first Trello card from the previously created list.
     * Uses the configuration property for the first card's ID, API key, and token.
     */
    @Test(dependsOnMethods = "updateTrelloCardOnListRandomly")
    public void deleteTrelloCardOnList() throws UnirestException, InterruptedException {
        String cardId = ConfigReader.getProperty("cardId1");
        sendDeleteRequest(CARDS_ENDPOINT + "/" + cardId);
    }

    /**
     * Deletes the second Trello card from the previously created list.
     * Uses the configuration property for the second card's ID, API key, and token.
     */
    @Test(dependsOnMethods = "deleteTrelloCardOnList")
    public void deleteTrelloCard2OnList() throws UnirestException, InterruptedException {
        String cardId = ConfigReader.getProperty("cardId2");
        sendDeleteRequest(CARDS_ENDPOINT + "/" + cardId);
    }

    /**
     * Deletes the entire Trello board created in the initial step.
     * Uses the configuration property for the board's ID, API key, and token.
     */
    @Test(dependsOnMethods = "deleteTrelloCard2OnList")
    public void deleteTrelloBoard() throws UnirestException, InterruptedException {
        String boardId = ConfigReader.getProperty("boardId");
        sendDeleteRequest(BOARD_ENDPOINT + "/" + boardId);
    }

    private HttpResponse<String> sendPostRequest(String url, Map<String, Object> params) throws UnirestException {
        return Unirest.post(url)
                .queryString(params)
                .asString();
    }

    private HttpResponse<String> sendPutRequest(String url, Map<String, Object> params) throws UnirestException {
        return Unirest.put(url)
                .queryString(params)
                .asString();
    }

    private HttpResponse<String> sendDeleteRequest(String url) throws UnirestException {
        return Unirest.delete(url)
                .asString();
    }
}
