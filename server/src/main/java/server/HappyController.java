package server;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/happiness")
public class HappyController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/{happinessLevel}")
    HttpResponse<String> happiness(String happinessLevel) {
        return HttpResponse.ok("Hello, your level of happiness is " + happinessLevel + "!");
    }
}