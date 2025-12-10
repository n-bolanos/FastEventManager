package steps;

import io.cucumber.java.en.*;
import okhttp3.*;
import org.junit.Assert;

import java.io.IOException;

public class DeleteEventSteps {

    private final OkHttpClient client = new OkHttpClient();
    private Response response;

    @Given("an event exists with id {string}")
    public void an_event_exists_with_id(String id) throws IOException {
        // Crear evento en backend Python
        RequestBody body = RequestBody.create(
                "{\"event_id\": " + id + ", \"name\": \"Test Event\", \"date\": \"2024-01-01\", \"location\": \"Online\"}",
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("http://localhost:8003/events")
                .post(body)
                .build();

        Response createResponse = client.newCall(request).execute();
        createResponse.close();
    }

    @Given("no event exists with id {string}")
    public void no_event_exists_with_id(String id) {
    }

    @When("the organizer deletes the event with id {string}")
    public void the_organizer_deletes_the_event_with_id(String id) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8003/events/" + id)
                .delete()
                .build();

        response = client.newCall(request).execute();
    }

    @Then("the system should respond with status {int}")
    public void the_system_should_respond_with_status(Integer status) {
        Assert.assertEquals(status.intValue(), response.code());
    }
}
