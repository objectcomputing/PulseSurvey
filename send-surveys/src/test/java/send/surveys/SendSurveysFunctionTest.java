package send.surveys;

import io.micronaut.test.annotation.MicronautTest;
import model.ResponseKey;
import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

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

    List<String> generateEmails(int size) {
        List<String> toReturn = new ArrayList<>(size);
        for (int count = 0; count < size; count++) {
            toReturn.add(RandomString.make(8) + "@" + RandomString.make(10) + ".com");
        }
        return toReturn;
    }

    List<ResponseKey> generateResponseKeys(int size) {
        List<ResponseKey> toReturn = new ArrayList<>(size);
        for (int count = 0; count < size; count++) {
            toReturn.add(new ResponseKey(UUID.randomUUID(), LocalDateTime.now()));
        }
        return toReturn;
    }

    /**
     * Ensure that the number of email addresses returned is the total number provided by
     * the GMail API.
     **/
    @Test
    void testGetTotalNumberOfAvailableEmailAddresses() {
        List<String> fakeEmails = generateEmails((int)Math.random()%50);
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        assertEquals(fakeEmails.size(), itemUnderTest.getTotalNumberOfAvailableEmailAddresses());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // have a list of emails,
    // test that it gets the right # and that they are
    @Test
    void testGetRandomEmailAddresses_CorrectNumber() {
        final int percentOfEmailsNeeded = 10;
        List<String> fakeEmails = generateEmails((int)Math.random()%50);
        final long numberOfEmailsToBeSent = (long) Math
                .ceil(fakeEmails.size() * (double) percentOfEmailsNeeded / 100.0);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals(numberOfEmailsToBeSent, itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).size());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // and that they are unique
    @Test
    void testGetRandomEmailAddresses_UniqueAddresses() {
        final int percentOfEmailsNeeded = 10;
        List<String> fakeEmails = generateEmails((int)Math.random()%50);
        final long numberOfEmailsToBeSent = (long) Math
                .ceil(fakeEmails.size() * (double) percentOfEmailsNeeded / 100.0);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals(numberOfEmailsToBeSent,
                itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded).stream().distinct().count());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // a subset of orig assertThat(actual, hasItems("b")); -or- assertThat(actual,
    // contains("a", "b", "c"));
    @Test
    void testGetRandomEmailAddresses_SubsetOfOriginal() {
        List<String> fakeEmails = generateEmails((int)Math.random()%50);
        final int percentOfEmailsNeeded = 10;

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        List<String> allAddresses = gmailApiMock.getEmails();

        assertTrue(allAddresses.containsAll(itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded)));
    }

    // generateKeys produces a list of uuid keys in string format
    // set up a list of uuid keys and see if a mock of this method
    // returns same make sure it returns x num of keys
    @Test
    void testGenerateKeys() {
        final int numkeys = (int)Math.random()%100;

        List<ResponseKey> actual = itemUnderTest.generateKeys(numkeys);
        assertEquals(numkeys, actual.size());
    }

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    // puts them together in a map
    @Test
    void testMapEmailsToKeys_CorrectNumber() {
        int numberOfEmails = (int)Math.random()%50;
        List<String> fakeEmails = generateEmails(numberOfEmails);
        List<ResponseKey> fakeKeys = generateResponseKeys(numberOfEmails);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals(fakeEmails.size(), itemUnderTest.mapEmailsToKeys(fakeEmails, fakeKeys).size());
    }

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    // puts them together in a map
    // check that the keys are all contained in the list that the mock was set up to
    // return
    @Test
    void testMapEmailsToKeys_ContainsCorrectKeys() {
        int numberOfEmails = (int)Math.random()%50;
        List<String> fakeEmails = generateEmails(numberOfEmails);
        List<ResponseKey> fakeKeys = generateResponseKeys(numberOfEmails);
        
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        
        Map<String, String> map = itemUnderTest.mapEmailsToKeys(fakeEmails,
        fakeKeys);
        
        assertTrue(map.keySet().containsAll(fakeEmails));
    }

    // sendTheEmails takes in the email/key map and sends out emails with the keys
    // * no idea how to test this one *
    @Test
    void testSendTheEmails() {
        // * no idea how to test this one *

    }

}
