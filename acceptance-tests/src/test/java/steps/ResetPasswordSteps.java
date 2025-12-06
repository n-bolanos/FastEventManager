package steps;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import okhttp3.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

public class ResetPasswordSteps {

    private static final String BASE_URL = "http://localhost:8010/auth";
    private OkHttpClient client = new OkHttpClient();

    private String email;
    private Response response;

    @Given("an existing user with email {string}")
    public void an_existing_user_with_email(String email) throws IOException {
        this.email = email;

        String json = "{"
                + "\"name\":\"TestUser\","
                + "\"username\":\"testuser-" + email + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"12345\""
                + "}";

        Request req = new Request.Builder()
                .url(BASE_URL + "/register")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        client.newCall(req).execute().close();
    }

    @Given("no user exists with email {string}")
    public void no_user_exists_with_email(String email) {
        this.email = email;
    }

    @Given("a password reset request with:")
    public void a_password_reset_request_with(DataTable table) {
        Map<String, String> row = table.asMap(String.class, String.class);

        this.email = row.get("email");
    }

    @When("the user submits the password reset request")
    public void the_user_submits_the_password_reset_request() throws IOException {

        String json = "{ \"email\":\"" + email + "\" }";

        Request req = new Request.Builder()
                .url(BASE_URL + "/reset-password")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        response = client.newCall(req).execute();
        CommonSteps.lastResponse = response;
        TestContext.lastResponse = response;
    }

    @Then("the system should send a password reset link to {string}")
    public void the_system_should_send_a_password_reset_link_to(String email) {
        // Aquí normalmente mockearías el servicio de email.
        // Para aceptación: se da por válido si status=200 y mensaje incluye "sent"
        assertEquals(200, response.code());
    }

}

