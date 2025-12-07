package steps;

import io.cucumber.java.en.*;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Assert;

public class ViewGuestsSteps {

    OkHttpClient client = new OkHttpClient();
    Response lastResponse;

    Gson gson = new Gson();

    String pythonBaseUrl = "http://localhost:8020"; 
    String gatewayUrl = "http://localhost:8010"; 

    @Given("the organizer is authenticated with user id {int}")
    public void authenticated(int userId) {
    }

    @Given("an event is created with name {string} and id {int} and has confirmed guests")
    public void createEventWithGuests(String name, int eventId) throws Exception {
        JsonObject eventJson = new JsonObject();
        eventJson.addProperty("id", eventId);
        eventJson.addProperty("name", name);

        RequestBody body = RequestBody.create(
                gson.toJson(eventJson),
                MediaType.parse("application/json")
        );

        Request createEvent = new Request.Builder()
                .url(pythonBaseUrl + "/events/")
                .post(body)
                .build();

        client.newCall(createEvent).execute();

        JsonArray guests = new JsonArray();
        JsonObject guest = new JsonObject();
        guest.addProperty("name", "Alice");
        guest.addProperty("email", "alice@example.com");
        guest.addProperty("status", "confirmed");
        guests.add(guest);

        RequestBody guestBody = RequestBody.create(
                gson.toJson(guests),
                MediaType.parse("application/json")
        );

        Request addGuests = new Request.Builder()
                .url(pythonBaseUrl + "/events/" + eventId + "/guests")
                .post(guestBody)
                .build();

        client.newCall(addGuests).execute();
    }

    @Given("an event is created with name {string} and id {int} and has no confirmed guests")
    public void createEventNoGuests(String name, int eventId) throws Exception {

        JsonObject eventJson = new JsonObject();
        eventJson.addProperty("id", eventId);
        eventJson.addProperty("name", name);

        Request createEvent = new Request.Builder()
                .url(pythonBaseUrl + "/events/")
                .post(RequestBody.create(
                        gson.toJson(eventJson),
                        MediaType.parse("application/json")
                ))
                .build();

        client.newCall(createEvent).execute();
    }

    @When("the organizer requests the confirmed guests of event {int}")
    public void requestGuests(int eventId) throws Exception {
        Request req = new Request.Builder()
                .url(gatewayUrl + "/attendance/event/" + eventId)
                .get()
                .build();

        lastResponse = client.newCall(req).execute();
    }

    @Then("the system should return status {int}")
    public void statusIs(int expected) {
        Assert.assertEquals(expected, lastResponse.code());
    }

    @Then("the response should contain a list of confirmed guests")
    public void containsGuests() throws Exception {
        String body = lastResponse.body().string();
        Assert.assertTrue(body.contains("email"));
        Assert.assertTrue(body.contains("name"));
    }

    @Then("the response should contain an empty guest list")
    public void emptyList() throws Exception {
        String body = lastResponse.body().string();
        Assert.assertTrue(body.contains("[]"));
    }
}
