package steps;

import io.cucumber.java.en.*;
import okhttp3.*;

import static org.junit.Assert.*;

public class AuthSteps {

    private final OkHttpClient client = new OkHttpClient();
    public static String authToken; 
    @Given("a default organizer user exists")
        public void a_default_organizer_user_exists() throws Exception {
            
            String json = "{"
                    + "\"name\":\"Organizer User\","
                    + "\"username\":\"organizer\","
                    + "\"email\":\"organizer@example.com\","
                    + "\"password\":\"12345\""
                    + "}";

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request req = new Request.Builder()
                    .url("http://localhost:8010/auth/register")
                    .post(body)
                    .build();

    client.newCall(req).execute().close();
}


    @Given("the organizer is authenticated")
public void the_organizer_is_authenticated() throws Exception {

        a_default_organizer_user_exists();

        String loginJson = "{ \"username\": \"organizer\", \"password\": \"12345\" }";

        RequestBody body = RequestBody.create(
                loginJson,
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url("http://localhost:8010/auth/login")
                .post(body)
                .build();

        Response res = client.newCall(req).execute();

        assertEquals(200, res.code());

        AuthSteps.authToken = res.body().string();
        assertNotNull(AuthSteps.authToken);
    }

}
