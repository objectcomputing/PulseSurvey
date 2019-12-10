package send.surveys;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
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
    // have a list of emails,
    // test that it gets the right # and that they are 
    @Test
    void testGetRandomEmailAddresses() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int percentOfEmailsNeeded = 5;
        final int numberOfEmailsReturned = 10;
        List<String> allAddresses = new ArrayList<String>();

        SendSurveysFunction.GmailApi gmailApiMock = mock(SendSurveysFunction.GmailApi.class);
        allAddresses = gmailApiMock.getEmails();
        assertEquals(numberOfEmailsReturned,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // and that they are unique
    @Test
    void testGetRandomEmailAddresses_UniqueAddresses() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int percentOfEmailsNeeded = 5;
        final int numberOfEmailsReturned = 10;

        assertEquals(numberOfEmailsReturned,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).stream().distinct().count());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // a subset of orig    assertThat(actual, hasItems("b")); -or- assertThat(actual, contains("a", "b", "c"));
    @Test
    void testGetRandomEmailAddresses_SubsetOfOriginal() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int percentOfEmailsNeeded = 5;
        final int numberOfEmailsReturned = 10;
        List<String> allAddresses = new ArrayList<String>();
//        addresses.add("williamsh@objectcomputing.com");
//        addresses.add("kimberlinm@objectcomputing.com");
//        addresses.add("patilm@objectcomputing.com");

        SendSurveysFunction.GmailApi gmailApiMock = mock(SendSurveysFunction.GmailApi.class);
        allAddresses = gmailApiMock.getEmails();
        assertEquals(numberOfEmailsReturned,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size());
    }

    // generateKeys produces a list of uuid keys in string format
    //      set up a list of uuid keys and see if a mock of this method
    //      returns same  make sure it returns x num of keys
    @Test
    void testGenerateKeys() {
        SendSurveysFunction itemUnderTest = new SendSurveysFunction();
        final int numkeys = 5;

        List<String> actual = itemUnderTest.generateKeys(numkeys);
        assertEquals(numkeys, actual.size());

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
