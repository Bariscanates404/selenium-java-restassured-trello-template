package org.example;

import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import utilities.ConfigReader;


public class TrelloTestUnitest {

    /**
     * Creates a new Trello board.
     * Uses the configuration properties for board name, API key, and token.
     * Sets the created board's ID in the configuration for future reference.
     */
    @Test (priority = 1)
    public void createTrelloBoard() throws UnirestException{
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
                .queryString("name", ConfigReader.getProperty("boardName"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("boardId", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates a new Trello list on the previously created board.
     * Uses the configuration properties for list name, board ID, API key, and token.
     * Sets the created list's ID in the configuration for future reference.
     */
    @Test (dependsOnMethods = "createTrelloBoard")
    public void createTrelloListOnBoard() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", ConfigReader.getProperty("listName"))
                .queryString("idBoard", ConfigReader.getProperty("boardId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("listId", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates the first Trello card on the previously created list.
     * Uses the configuration properties for card name, list ID, API key, and token.
     * Sets the created card's ID in the configuration for future reference.
     */
    @Test (dependsOnMethods = "createTrelloListOnBoard")
    public void createTrelloCard1OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", ConfigReader.getProperty("cardName1"))
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId1", jsonResponse.get("id").getAsString());
    }

    /**
     * Creates the second Trello card on the previously created list.
     * Uses the configuration properties for card name, list ID, API key, and token.
     * Sets the created card's ID in the configuration for future reference.
     */
    @Test (dependsOnMethods = "createTrelloCard1OnList" )
    public void createTrelloCard2OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", ConfigReader.getProperty("cardName2"))
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId2", jsonResponse.get("id").getAsString());
    }

    /**
     * Updates a randomly selected Trello card's name and color on the previously created list.
     * Uses a random number to choose between the first and second card.
     * Uses the configuration properties for card IDs, list ID, API key, and token.
     */
    @Test (dependsOnMethods = "createTrelloCard2OnList")
    public void updateTrelloCardOnListRandomly() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        String cardID;
        int tmp = (int) (Math.random() * 1) + 1;
        if (tmp == 1) {
            cardID = ConfigReader.getProperty("cardId1");
        } else {
            cardID = ConfigReader.getProperty("cardId2");
        }
        String urlUpdated = "https://api.trello.com/1/cards/" + cardID;

        HttpResponse<JsonNode> response = Unirest.put(urlUpdated)
                .header("Accept", "application/json")
                .queryString("id", cardID)
                .queryString("name", "Trello Card Updated")
                .queryString("color", "blue")
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

    }

    /**
     * Deletes the first Trello card from the previously created list.
     * Uses the configuration property for the first card's ID, API key, and token.
     */
    @Test (dependsOnMethods = "updateTrelloCardOnListRandomly")
    public void deleteTrelloCardOnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId1"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();
    }

    /**
     * Deletes the second Trello card from the previously created list.
     * Uses the configuration property for the second card's ID, API key, and token.
     */
    @Test (dependsOnMethods = "deleteTrelloCardOnList")
    public void deleteTrelloCard2OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId2"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

    }

    /**
     * Deletes the entire Trello board created in the initial step.
     * Uses the configuration property for the board's ID, API key, and token.
     */
    @Test (dependsOnMethods = "deleteTrelloCard2OnList")
    public void deleteTrelloBoard() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + ConfigReader.getProperty("boardId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

    }
}

