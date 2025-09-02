package to.coul.daemons.http;

import jakarta.ws.rs.*;

@Path("/tasks")
public class TasksResource {

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}

