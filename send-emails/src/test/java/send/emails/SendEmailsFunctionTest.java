package send.emails;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class SendEmailsFunctionTest {

    @Inject
    SendEmailsClient client;

    @Test
    public void testFunction() throws Exception {
    	SendEmails body = new SendEmails();
    	body.setName("send-emails");
        assertEquals("send-emails", client.apply(body).blockingGet().getName());
    }
}
