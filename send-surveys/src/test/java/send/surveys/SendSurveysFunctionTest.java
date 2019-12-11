package send.surveys;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
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
    SendSurveysFunction itemUnderTest;

    SendSurveysFunction.GmailApi gmailApiMock = mock(SendSurveysFunction.GmailApi.class);

    @BeforeEach
    void setupTest() {
        itemUnderTest.setGmailApi(gmailApiMock);
        reset(gmailApiMock);
    }

    @Test
    void testFunction() throws Exception {
        List<String> fakeEmails = Arrays.asList("a@oci.com", "b@oci.com",
                "c@oci.com", "d@oci.com","e@oci.com","f@oci.com","g@oci.com",
                "h@oci.com","i@oci.com","j@oci.com", "k@oci.com");
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals("I'm gonna send surveys, eventually.", itemUnderTest.get().getName());
    }

    // getTotalNumberOfAvailableEmailAddresses is the same as the
    //      set up a number and see if method returns same

    @Test
    void testGetTotalNumberOfAvailableEmailAddresses() {
        List<String> fakeEmails = Arrays.asList("a@oci.com", "b@oci.com",
                "c@oci.com", "d@oci.com","e@oci.com","f@oci.com","g@oci.com",
                "h@oci.com","i@oci.com","j@oci.com", "k@oci.com","l@oci.com","m@oci.com", "n@oci.com");
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        assertEquals(fakeEmails.size(), itemUnderTest.getTotalNumberOfAvailableEmailAddresses());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // have a list of emails,
    // test that it gets the right # and that they are

    @Test
    void testGetRandomEmailAddresses_CorrectNumber() {
        final int percentOfEmailsNeeded = 10;
        List<String> fakeEmails = Arrays.asList("a@oci.com", "b@oci.com",
                "c@oci.com", "d@oci.com","e@oci.com","f@oci.com","g@oci.com",
                "h@oci.com","i@oci.com","j@oci.com", "k@oci.com","l@oci.com","m@oci.com", "n@oci.com");
        final long numberOfEmailsToBeSent = (long) Math.ceil(fakeEmails.size() * (double) percentOfEmailsNeeded / 100.0);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        System.out.println("numberOfEmailsToBeSent: " + numberOfEmailsToBeSent);

        System.out.println("itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size(): " +
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size());

        assertEquals(numberOfEmailsToBeSent,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // and that they are unique
    @Test
    void testGetRandomEmailAddresses_UniqueAddresses() {
        final int percentOfEmailsNeeded = 10;
        List<String> fakeEmails = Arrays.asList("a@oci.com", "b@oci.com",
                "c@oci.com", "d@oci.com","e@oci.com","f@oci.com","g@oci.com",
                "h@oci.com","i@oci.com","j@oci.com", "k@oci.com","l@oci.com",
                "m@oci.com", "n@oci.com");
        final long numberOfEmailsToBeSent = (long) Math.ceil(fakeEmails.size() * (double) percentOfEmailsNeeded / 100.0);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals(numberOfEmailsToBeSent,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).stream().distinct().count());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // a subset of orig    assertThat(actual, hasItems("b")); -or- assertThat(actual, contains("a", "b", "c"));
    @Test
    void testGetRandomEmailAddresses_SubsetOfOriginal() {
        List<String> fakeEmails = Arrays.asList("a@oci.com", "b@oci.com",
                "c@oci.com", "d@oci.com","e@oci.com","f@oci.com","g@oci.com",
                "h@oci.com","i@oci.com","j@oci.com", "k@oci.com","l@oci.com",
                "m@oci.com", "n@oci.com");
        final int percentOfEmailsNeeded = 10;
        final long numberOfEmailsToBeSent = (long) Math.ceil(fakeEmails.size() * (double) percentOfEmailsNeeded / 100.0);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        List<String> allAddresses = gmailApiMock.getEmails();
//
//        System.out.println("all addresses: ");
//        allAddresses.stream().forEach(System.out::println);
//        System.out.println("subset of addresses: ");
//        itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).stream().forEach(System.out::println);

        assertTrue(allAddresses.containsAll(itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded)));
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
    // check length
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

    // storeKeysInDb takes in the list of keys and stores it in the db 
    //      * no idea how to test this one *
    @Test
    void testStoreKeysInDb() {
        //      * no idea how to test this one *

    }

    // sendTheEmails takes in the email/key map and sends out emails with the keys
    //      * no idea how to test this one *
    @Test
    void testSendTheEmails() {
        //      * no idea how to test this one *

    }

}
