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
    void testFunction() throws Exception {
        assertEquals("I'm gonna send surveys, eventually.", client.get().blockingGet().getName());
    }

    // getTotalNumberOfAvailableEmailAddresses is the same as the
    //      set up a number and see if method returns same

    @Test
    void testGetTotalNumberOfAvailableEmailAddresses() {

        // mock call to google api and return a number
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        assertEquals(170, itemUnderTest.getTotalNumberOfAvailableEmailAddresses());
    }

    // getRandomEmailAddresses produces a list of email addresses
    //      set up a list of emails and see if this method returns same

    // generateKeys produces a list of uuid keys in string format
    //      set up a list of uuid keys and see if this method returns same

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    //      puts them together in a map


    // sendTheEmails takes in the email/key map and sends out emails with the keys
    //      * no idea how to test this one *



}
