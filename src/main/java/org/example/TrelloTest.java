package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TrelloTest {


    String APIKey = "84fc4150414f99009bcdba78f02fa3ee";
    String APIToken = "ATTAed1feee91d3e5479daa56d0bc7b43b4b01b3b71df363baa147bdbf60ed72ddec34D71F34";
    String boardName = "{fromJavaBoard}";
    String boardId = "65a4512a54f9b5378ba425f1";
    String listName = "{fromJavaList}";
    String listId = "63f17730d00d0e529ddf6957";
    String cardName1 = "{fromJavaCard1}";
    String cardId2 = "63f17804bbbc7b2e1f5e2820";
    String cardName2 = "{fromJavaCard2}";
    String cardId1 = "63f1784cdeca3847104b23a3";


    @Test
    public void createTrelloBoard() throws UnirestException, InterruptedException {

        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
                .queryString("name", boardName)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        System.out.println("1" + boardId);
        // Parse the JSON response using Gson
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        boardId = jsonResponse.get("id").getAsString();
        Thread.sleep(2500);
        System.out.println("2" + boardId);
    }

    @Test
    public void createTrelloListOnBoard() throws UnirestException, InterruptedException {
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", listName)
                .queryString("idBoard", "65a45291023f8a694401ab33")
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        System.out.println("1" + listId);
        // Parse the JSON response using Gson
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        listId = jsonResponse.get("id").getAsString();
        Thread.sleep(2500);
        System.out.println("2" + listId);

    }


    @Test
    public void createTrelloCard1OnList() throws UnirestException, InterruptedException {
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName1)
                .queryString("idList", "65a4532634d0c5d25b7b2a4d")
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        System.out.println(response.getBody());
        System.out.println("Response Body: " + response.getBody());
        System.out.println("1" + cardId1);
        // Parse the JSON response using Gson
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        cardId1 = jsonResponse.get("id").getAsString();
        Thread.sleep(2500);
        System.out.println("2" + cardId1);


    }

    @Test
    public void createTrelloCard2OnList() throws UnirestException, InterruptedException {
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName2)
                .queryString("idList", listId)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();
        Thread.sleep(2500);
        System.out.println(response.getBody());
        System.out.println("1" + cardId2);
        // Parse the JSON response using Gson
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        cardId2 = jsonResponse.get("id").getAsString();
        Thread.sleep(2500);
        System.out.println("2" + cardId2);


    }

    @Test
    public void updateTrelloCardOnListRandomly() throws UnirestException, InterruptedException {
        String cardID;
        int tmp = (int) (Math.random() * 1) + 1;
        if (tmp == 1) {
            cardID = cardId1;
        } else {
            cardID = cardId2;
        }
        String urlUpdated = "https://api.trello.com/1/cards/" + cardID;

        HttpResponse<JsonNode> response = Unirest.put(urlUpdated)
                .header("Accept", "application/json")
                .queryString("id", cardID)
                .queryString("name", "{fromJavaUpdated}")
                .queryString("color", "yellow")
                .queryString("idList", listId)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        Thread.sleep(2500);
        System.out.println(response.getBody());
    }

    @Test
    public void deleteTrelloCardOnList() throws UnirestException, InterruptedException {
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + cardId1)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        Thread.sleep(2500);
        System.out.println(response.getBody());
    }

    public void deleteTrelloCard2OnList() throws UnirestException, InterruptedException {
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + cardId2)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        Thread.sleep(2500);
        System.out.println(response.getBody());
    }

    @Test
    public void deleteTrelloBoard() throws UnirestException, InterruptedException {
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + boardId)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        Thread.sleep(2500);
        System.out.println(response.getBody());
    }
}

