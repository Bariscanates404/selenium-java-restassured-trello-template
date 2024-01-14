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
import utilities.ConfigReader;

import java.util.List;
import java.util.Random;

public class TrelloTest {

//configreadera tasi
    String APIKey = "84fc4150414f99009bcdba78f02fa3ee";
    String APIToken = "ATTAed1feee91d3e5479daa56d0bc7b43b4b01b3b71df363baa147bdbf60ed72ddec34D71F34";
    String boardName = "{fromJavaBoard}";
    String listName = "{fromJavaList}";
    String cardName1 = "{fromJavaCard1}";
    String cardName2 = "{fromJavaCard2}";


    @Test
    public void a() throws UnirestException, InterruptedException {

        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
                .queryString("name", boardName)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("boardId", jsonResponse.get("id").getAsString());
    }

    @Test
    public void b() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", listName)
                .queryString("idBoard", ConfigReader.getProperty("boardId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("listId", jsonResponse.get("id").getAsString());
    }


    @Test
    public void c() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName1)
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId1", jsonResponse.get("id").getAsString());
    }

    @Test
    public void d() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName2)
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId2", jsonResponse.get("id").getAsString());
    }

    @Test
    public void f() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
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
                .queryString("name", "{fromJavaUpdated}")
                .queryString("color", "yellow")
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

    }

    @Test
    public void g() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId1"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();
    }

    public void h() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId2"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

    }

    @Test
    public void y() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + ConfigReader.getProperty("boardId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

    }
}

