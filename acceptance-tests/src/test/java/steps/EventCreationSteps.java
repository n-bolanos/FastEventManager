package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import okhttp3.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

public class EventCreationSteps {

    private static final String EVENTS_BASE_URL = "http://localhost:8020";
    private static final MediaType JSON = MediaType.parse("application/json");

    private OkHttpClient client = new OkHttpClient();

    private String eventJson;
    private Response response;

    @Given("an event creation request with:")
    public void an_event_creation_request_with(DataTable data) {
        Map<String, String> row = data.asMaps().get(0);

        eventJson = "{"
                + "\"name\":\"" + row.get("name") + "\","
                + "\"description\":\"" + row.get("description") + "\","
                + "\"date\":\"" + row.get("date") + "\","
                + "\"location\":\"" + row.get("location") + "\""
                + "}";
    }

    @When("the organizer submits the event creation request")
    public void the_organizer_submits_the_event_creation_request() throws IOException {

        RequestBody body = RequestBody.create(eventJson, JSON);

        Request request = new Request.Builder()
                .url(EVENTS_BASE_URL)
                .post(body)
                .build();

        response = client.newCall(request).execute();
    }

    @Then("the system should save the event in the database")
    public void the_system_should_save_the_event_in_the_database() {
        assertEquals(201, response.code());
    }
}
