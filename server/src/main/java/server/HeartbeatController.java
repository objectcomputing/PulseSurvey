package server;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/heartbeat") 
public class HeartbeatController {
    @Get("/") 
    @Produces(MediaType.TEXT_PLAIN) 
    public String index() {
        return "I am Alive!"; 
    }
}