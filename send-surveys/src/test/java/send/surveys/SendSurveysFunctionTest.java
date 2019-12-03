package send.surveys;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class SendSurveysFunctionTest {

    @Inject
    SendSurveysClient client;

    @Test
    public void testFunction() throws Exception {
        assertEquals("I'm gonna send surveys, eventually.", client.get().blockingGet().getName());
    }
}
