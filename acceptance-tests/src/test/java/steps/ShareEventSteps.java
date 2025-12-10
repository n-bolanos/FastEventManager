package steps;

import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class ShareEventSteps {

    private static class ClipboardMock {
        private static String copiedText = "";

        static void copy(String text) {
            copiedText = text;
        }

        static String getCopiedText() {
            return copiedText;
        }
    }

    private final Map<String, String> eventLinks = new HashMap<>();

    @Given("an event exists with id {string} and share link {string}")
    public void an_event_exists_with_id_and_share_link(String id, String link) {
        eventLinks.put(id, link);
    }

    @When("the organizer clicks the Share event button for id {string}")
    public void the_organizer_clicks_the_share_event_button_for_id(String id) {
        String link = eventLinks.get(id);
        ClipboardMock.copy(link);
    }

    @Then("the system should copy {string} to the clipboard")
    public void the_system_should_copy_to_the_clipboard(String expectedLink) {
        Assert.assertEquals(expectedLink, ClipboardMock.getCopiedText());
    }
}
