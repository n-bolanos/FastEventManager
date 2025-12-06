package steps;

import io.cucumber.java.en.*;
import okhttp3.*;

import static org.junit.Assert.*;

public class ViewGuestsSteps {

    private Response response;
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "http://localhost:8080";

    @Given("there is an event with id {string} that has confirmed guests")
    public void eventWithGuestsExists(String eventId) {
    }

    @Given("there is an event with id {string} that has no confirmed guests")
    public void eventWithoutGuestsExists(String eventId) {
    }

    @When("the organizer selects the event with id {string}")
    public void selectEvent(String eventId) throws Exception {
        Request request = new Request.Builder()
                .url(baseUrl + "/events/" + eventId + "/guests/confirmed")
                .get()
                .build();

        response = client.newCall(request).execute();
    }

    @Then("the system returns the confirmed guests list with their personal information")
    public void guestsListReturned() throws Exception {
        assertEquals(200, response.code());

        String body = response.body().string();
        assertNotNull(body);
        assertTrue(body.contains("name"));
        assertTrue(body.contains("email"));
        assertTrue(body.contains("phone"));
    }

    @Then("the system returns an empty confirmed guest list")
    public void emptyGuestListReturned() throws Exception {
        assertEquals(200, response.code());

        String body = response.body().string();
        assertNotNull(body);
        assertTrue(body.equals("[]") || body.contains("\"guests\":[]"));
    }
}

