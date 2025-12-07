package steps;

import io.cucumber.java.en.Then;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonSteps {

    public static Response lastResponse;

    @Then("the response should contain message {string}")
    public void the_response_should_contain_message(String expectedMessage) throws Exception {
        String body = lastResponse.body().string();
        assertTrue(body.contains(expectedMessage));
        
    }
    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) throws Exception {
        assertEquals(expectedStatus.intValue(), TestContext.lastResponse.code());
    }
}
