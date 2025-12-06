package steps;

import io.cucumber.java.en.*;
import okhttp3.*;

import static org.junit.Assert.*;

public class ViewGuestsSteps {

    private Response response;
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "http://localhost:8080"; // ajusta al servicio real

    @Given("there is an event with id {string} that has confirmed guests")
    public void eventWithGuestsExists(String eventId) {
        // Opcional: crear evento y subir invitados para pruebas
        // Aquí solo se deja como placeholder si no tienes endpoint de testing
    }

    @Given("there is an event with id {string} that has no confirmed guests")
    public void eventWithoutGuestsExists(String eventId) {
        // Placeholder, igual que arriba
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

        // Opcional: validar campos esperados
        assertTrue(body.contains("name"));
        assertTrue(body.contains("email"));
        assertTrue(body.contains("phone"));
    }

    @Then("the system returns an empty confirmed guest list")
    public void emptyGuestListReturned() throws Exception {
        assertEquals(200, response.code());

        String body = response.body().string();
        assertNotNull(body);

        // Puedes validar JSON vacío
        assertTrue(body.equals("[]") || body.contains("\"guests\":[]"));
    }
}
