package send.surveys;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    //      set up a list of emails and see if this method returns same(?)
    // current state of test is dubious (defn 2)
    @Test
    void testGetRandomEmailAddresses() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int perOfTot = 5;
        List<String> addresses = new ArrayList<String>();
        addresses.add("williamsh@objectcomputing.com");
        addresses.add("kimberlinm@objectcomputing.com");
        addresses.add("patilm@objectcomputing.com");

        assertEquals(addresses, itemUnderTest.getRandomEmailAddresses(perOfTot));
    }

    // generateKeys produces a list of uuid keys in string format
    //      set up a list of uuid keys and see if a mock of this method
    //      returns same
    @Test
    void testGenerateKeys() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int perOfTot = 5;
        List<String> keys = new ArrayList<String>();
        keys.add("abc123");
        keys.add("xyz987");
        keys.add("lmn456");

        when(UUID.randomUUID().toString()).thenReturn(String.valueOf(keys));
    }

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    //      puts them together in a map
    @Test
    void testMapEmailsToKeys() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        List<String> addresses = new ArrayList<String>();
        addresses.add("williamsh@objectcomputing.com");
        addresses.add("kimberlinm@objectcomputing.com");
        addresses.add("patilm@objectcomputing.com");
        List<String> keys = new ArrayList<String>();
        keys.add("abc123");
        keys.add("xyz987");
        keys.add("lmn456");

        Map<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < 3; i++) {
            map.put(addresses.get(i), keys.get(i));
        }

        assertEquals(map, itemUnderTest.mapEmailsToKeys(addresses, keys));

    }

    // sendTheEmails takes in the email/key map and sends out emails with the keys
    //      * no idea how to test this one *
    @Test
    void testSendTheEmails() {
        //      * no idea how to test this one *

    }

}
