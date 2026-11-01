# selenium-java-restassured-trello-template

TestNG + REST Assured template that exercises the Trello REST API end to end.

## What it is

A small Maven project with two test classes that create a Trello board, a list and two cards, update and delete cards through the Trello API, passing ids between dependent tests. Settings are read from `configuration.properties` through `ConfigReader`.

## Why it exists

A reusable API-test template (REST API chaining, config-driven data) originally written as a Trello API test assignment (the Maven artifact is called `TestiniumTrelloAPI`).

## Contents

| Path | Purpose |
| --- | --- |
| `pom.xml` | Maven build (Java 17): REST Assured 5.3.0, TestNG 7.8.0, Unirest, Gson |
| `src/main/java/org/example/TrelloTestRestAssured.java` | REST Assured test chain: create board, list, cards; update; delete |
| `src/main/java/org/example/TrelloTestUnitest.java` | The same flow using Unirest |
| `src/main/java/utilities/ConfigReader.java` | Reads `configuration.properties` |
| `configuration.properties` | Trello base URL, API credentials, board/list/card names and ids |

## How to run / use

1. Put your own Trello API key and token in `configuration.properties`. Never commit real credentials.
2. Run the tests (the test classes live under `src/main`, so run them from the IDE as TestNG classes, or point Maven at them):

```
mvn clean compile
```

## Tech stack

Java 17, Maven, TestNG, REST Assured, Unirest, Gson.

## Status

Template, low activity. Last commit 2025-01-10, 7 commits since 2024-01-15.

## Related repos

`Selenium-java-pom-report-crossbrowser-template` (contains a copy of this Trello test and the same config layout).
