package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import okhttp3.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

public class LoginSteps {

    private LoginRequest request;
    private Response response;

    OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8010/auth/login";
    private static final String REGISTER_URL = "http://localhost:8010/auth/register";

    // -----------------------------
    // GIVEN
    // -----------------------------

    @Given("a user with username {string} and password {string} exists")
    public void a_user_exists(String username, String password) throws IOException {

        String email = username + "@example.com";

        String json = "{"
                + "\"name\":\"TestUser\","
                + "\"username\":\"" + username + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .build();

        client.newCall(req).execute().close();
    }

    @Given("a login request with:")
    public void a_login_request_with(DataTable data) {
        Map<String, String> row = data.asMaps().get(0);

        request = new LoginRequest(
                row.get("username"),
                row.get("password")
        );
    }

    // -----------------------------
    // WHEN
    // -----------------------------

    @When("the user submits the login request")
    public void the_user_submits_the_login_request() throws IOException {

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

    // -----------------------------
    // THEN
    // -----------------------------

    @Then("the system should authenticate the user")
    public void the_system_should_authenticate_the_user() {
        assertEquals(200, response.code());
    }

    @Then("the response should contain a token")
    public void the_response_should_contain_a_token() throws IOException {
        String json = response.body().string();
        assertTrue(json.contains("token"));
    }
}
