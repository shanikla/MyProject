package com.myproject.web;

import com.myproject.utils.DataCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class EventRestService {

    private final Logger log = LoggerFactory.getLogger(EventRestService.class);

    /**
     * Hello World for this rest service
     *
     * @return String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @SuppressWarnings("SameReturnValue")
    public String sayHello() {
        return "Hello, counting events...";
    }

    /**
     * Get event word counts.
     *
     * @return Response
     */
    @GET
    @Path("/words")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventWords() {
        log.debug("getting event words count");
        return Response.status(Response.Status.OK).entity(
                DataCounter.getInstance().getEventWordCount())
                .build();
    }

    /**
     * Get event word counts.
     *
     * @return Response
     */
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventTypes() {
        log.debug("getting event type count");
        return Response.status(Response.Status.OK).entity(
                DataCounter.getInstance().getEventTypeCount())
                .build();
    }
}