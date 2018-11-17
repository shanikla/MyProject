import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.stream.Event;
import com.myproject.utils.DataCounter;
import com.myproject.utils.JsonResponse;
import com.myproject.web.EventRestService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventTest extends JerseyTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void stage1_hello() {
        final Response response = target("/").request().get();

        String responseString = response.readEntity(String.class);
        Assert.assertEquals("Hello, counting events...", responseString);
    }

    @Test
    public void stage2_insertSuccessJson() {
        // add count - 1 json
        Event event = new Event("foo", "fighters", 123L);
        try {
            DataCounter.getInstance().handleEvent(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            Assert.fail(e.getMessage());
        }

        // test count words response
        final Response responseWords = target("/words").request().get();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), responseWords.getStatus());
        String responseString = responseWords.readEntity(String.class);
        JsonResponse expectedTypeJson = new JsonResponse();
        expectedTypeJson.addCount("fighters", 1L);
        try {
            JsonResponse actualResponse = mapper.readValue(responseString, JsonResponse.class);
            Assert.assertEquals(expectedTypeJson, actualResponse);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        // test count types response
        final Response responseTypes = target("/types").request().get();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), responseTypes.getStatus());
        responseString = responseTypes.readEntity(String.class);
        expectedTypeJson = new JsonResponse();
        expectedTypeJson.addCount("foo", 1L);
        try {
            JsonResponse actualResponse = mapper.readValue(responseString, JsonResponse.class);
            Assert.assertEquals(expectedTypeJson, actualResponse);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void stage3_insertFailedJson() {
        // get original responses
        final Response origResponseWords = target("/words").request().get();
        String origResponseWordsStr = origResponseWords.readEntity(String.class);
        final Response origResponseTypes = target("/types").request().get();
        String origResponseTypesStr = origResponseTypes.readEntity(String.class);

        // send corrupt json
        DataCounter.getInstance().handleEvent("{ \"corrupt\": \"fake\"}");

        // check that the corrupted Json wasn't counted
        final Response actualResponseWords = target("/words").request().get();
        String responseString = actualResponseWords.readEntity(String.class);
        Assert.assertEquals(origResponseWordsStr, responseString);

        final Response actualResponseTypes = target("/types").request().get();
        responseString = actualResponseTypes.readEntity(String.class);
        Assert.assertEquals(origResponseTypesStr, responseString);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(EventRestService.class);
    }
}
