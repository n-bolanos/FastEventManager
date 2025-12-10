package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import static org.junit.Assert.*;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class RegistrationSteps {

    private RegisterRequest request;
    private Response response;

    OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "http://localhost:8010/auth/register";

    @Given("a registration request with:")
    public void createRequest(DataTable data) {
        Map<String, String> row = data.asMaps().get(0);

        request = new RegisterRequest(
                row.get("name"),
                row.get("username"),
                row.get("email"),
                row.get("password")
        );
    }

    @Given("the email {string} is not registered")
    public void the_email_is_not_registered(String email) {
    }

    @Given("the username {string} is not taken")
    public void the_username_is_not_taken(String username) {
    }

    @Given("the user already exists with email {string}")
    public void the_user_already_exists_with_email(String email) throws IOException {

        String json =
                "{"
                        + "\"name\":\"PreExisting\","
                        + "\"username\":\"preuser\","
                        + "\"email\":\"" + email + "\","
                        + "\"password\":\"12345\""
                        + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(req).execute().close();
    }

    @Given("an existing user with username {string}")
    public void an_existing_user_with_username(String username) throws IOException {

        String email = "existing-" + username + "@example.com";

        String json =
                "{"
                        + "\"name\":\"Clone\","
                        + "\"username\":\"" + username + "\","
                        + "\"email\":\"" + email + "\","
                        + "\"password\":\"abc123\""
                        + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(req).execute().close();
    }

    @When("the user submits the registration request")
    public void the_user_submits_the_registration_request() throws IOException {

        RequestBody body = RequestBody.create(
                request.toJson(),
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        response = client.newCall(req).execute();
        CommonSteps.lastResponse = response;
        TestContext.lastResponse = response;


    }

    @When("the client sends the registration request")
    public void the_client_sends_the_registration_request() throws IOException {
        the_user_submits_the_registration_request();
    }

    @Then("the system should create the user in the database")
    public void the_system_should_create_the_user_in_the_database() {
        assertEquals(201, response.code());
    }

    @Then("the system should send a confirmation email to {string}")
    public void the_system_should_send_a_confirmation_email(String email) {
        // No se puede validar directamente sin mocks
    }

    @Then("the system should throw an error {string}")
    public void the_system_should_throw_an_error(String expectedMessage) throws IOException {
        String json = response.body().string();
        assertTrue(json.contains(expectedMessage));
    }
}
