package com.objectcomputing.pulsesurvey.send.surveys;

import com.objectcomputing.pulsesurvey.email.manager.GmailSender;
import com.objectcomputing.pulsesurvey.model.SendSurveysCommand;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;
import io.micronaut.test.annotation.MicronautTest;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SystemPropertyExtension;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
//import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SystemPropertyExtension.class)
@MicronautTest
public class SurveysControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SurveysControllerTest.class);

    @Inject
    SurveysController itemUnderTest;

    SurveysController.GmailApi gmailApiMock = mock(SurveysController.GmailApi.class);

    ResponseKeyRepository mockRepository = mock(ResponseKeyRepository.class);

    SurveyTemplateManager mockTemplateManager = mock(SurveyTemplateManager.class);

    GmailSender mockGmailSender = mock(GmailSender.class);

    @BeforeEach
    void setupTest() {
        itemUnderTest.setGmailApi(gmailApiMock);
        itemUnderTest.setResponseKeyRepo(mockRepository);
        itemUnderTest.setTemplateManager(mockTemplateManager);
        itemUnderTest.setGmailSender(mockGmailSender);
        reset(gmailApiMock);
        reset(mockRepository);
        reset(mockTemplateManager);
        reset(mockGmailSender);
    }

    List<String> generateFakeEmails(int size) {
        List<String> toReturn = new ArrayList<>(size);
        for (int count = 0; count < size; count++) {
            toReturn.add(RandomString.make(8) + "@" + RandomString.make(10) + ".com");
        }
        return toReturn;
    }

    List<ResponseKey> generateFakeResponseKeys(int size) {
        List<ResponseKey> toReturn = new ArrayList<>(size);
        for (int count = 0; count < size; count++) {
            toReturn.add(new ResponseKey(UUID.randomUUID(), LocalDateTime.now(), false));
        }
        return toReturn;
    }

 //   static final int PERCENT_OF_EMAILS = 10;

    @Test
    void testSendEmails_ReportsCorrectNumber() {
        String percentOfEmails = "10";
        double doublePercentOfEmails = parseDouble(percentOfEmails);
        LOG.info("Using doublePercentOfEmails: "+ doublePercentOfEmails);
        List<String> fakeEmails = generateFakeEmails((int)(Math.random()*100)%50);
        LOG.info("Using addresses: "+ fakeEmails.size());
        final int numberOfEmailsToBeSent = (int) Math
                .ceil(fakeEmails.size() * parseDouble(percentOfEmails) / 100.0);
        final int numkeys = (int)(Math.random()*100)%100;
        LOG.info("Using numberOfEmailsToBeSent: "+ numberOfEmailsToBeSent);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        List<ResponseKey> fakeKeys = generateFakeResponseKeys(numkeys);
        when(mockRepository.saveAll(any())).thenReturn(fakeKeys);

        String fakeTemplateName = "not a real template";
        Map<String, String> fakeEmailKeyMap = new  HashMap<>();
        fakeEmailKeyMap.put("one@fake.email", "fake key 1");
        fakeEmailKeyMap.put("two@fake.email", "fake key 2");
        Map<String, String> fakeEmailBodiesMap = new  HashMap<>();
        fakeEmailBodiesMap.put("one@fake.email", "fake email body 1");
        fakeEmailBodiesMap.put("two@fake.email", "fake email body 2");

        try {
            when(mockTemplateManager.populateEmails(fakeTemplateName, fakeEmailKeyMap)).thenReturn(fakeEmailBodiesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SendSurveysCommand sendSurveysCommand = new SendSurveysCommand();
        sendSurveysCommand.setTemplateName("emailTemplate");
        sendSurveysCommand.setPercentOfEmails(percentOfEmails);
        SendSurveys sent = itemUnderTest.sendEmails(sendSurveysCommand);

        assertThat(sent.getName(), containsString("Sent surveys:"));
        assertThat(sent.getName(), containsString("Sent surveys: "+numberOfEmailsToBeSent));
    }

    /**
     * Ensure that the number of email addresses returned is the total number provided by
     * the GMail API.
     **/
    @Test
    void testGetTotalNumberOfAvailableEmailAddresses() {
        List<String> fakeEmails = generateFakeEmails((int)(Math.random()*100)%50);
        LOG.info("Using addresses: "+ fakeEmails.size());
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        assertEquals(fakeEmails.size(), itemUnderTest.getTotalNumberOfAvailableEmailAddresses());
    }

    // getRandomEmailAddresses produces a list of email addresses
    // have a list of emails,
    // test that it gets the right # and that they are
    @Test
    void testGetRandomEmailAddresses_CorrectNumber() {
        final int percentOfEmailsNeeded = 10;
        List<String> fakeEmails = generateFakeEmails((int)(Math.random()*100)%50);
        LOG.info("Using addresses: "+ fakeEmails.size());
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
        List<String> fakeEmails = generateFakeEmails((int)(Math.random()*100)%50);
        LOG.info("Using addresses: "+ fakeEmails.size());
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
        List<String> fakeEmails = generateFakeEmails((int)(Math.random()*100)%50);
        LOG.info("Using addresses: "+ fakeEmails.size());
        final int percentOfEmailsNeeded = 10;

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        List<String> allAddresses = gmailApiMock.getEmails();

        assertTrue(allAddresses.containsAll(itemUnderTest.getRandomEmailAddresses(percentOfEmailsNeeded)));
    }

    // generateKeys produces a list of uuid keys in string format
    // set up a list of uuid keys and see if a mock of this method
    // returns same make sure it returns x num of keys
    @Test
    void testGenerateAndSaveKeys() {
        final int numkeys = (int)(Math.random()*100)%100;
        LOG.info("Generating keys: "+ numkeys);

        List<ResponseKey> fakeKeys = generateFakeResponseKeys(numkeys);

        when(mockRepository.saveAll(any())).thenReturn(fakeKeys);

        List<ResponseKey> actual = itemUnderTest.generateAndSaveKeys(numkeys);
        assertEquals(numkeys, actual.size());
    }

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    // puts them together in a map
    @Test
    void testMapEmailsToKeys_CorrectNumber() {
        int numberOfEmails = (int)(Math.random()*100)%50;
        LOG.info("Mapping emails: "+ numberOfEmails);
        List<String> fakeEmails = generateFakeEmails(numberOfEmails);
        List<ResponseKey> fakeKeys = generateFakeResponseKeys(numberOfEmails);

        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);

        assertEquals(fakeEmails.size(), itemUnderTest.mapEmailsToKeys(fakeEmails, fakeKeys).size());
    }

    // mapEmailsToKeys takes a list of email addresses and a list of keys and
    // puts them together in a map
    // check that the keys are all contained in the list that the mock was set up to
    // return
    @Test
    void testMapEmailsToKeys_ContainsCorrectKeys() {
        int numberOfEmails = (int)(Math.random()*100)%50;
        LOG.info("Mapping emails: "+ numberOfEmails);
        List<String> fakeEmails = generateFakeEmails(numberOfEmails);
        List<ResponseKey> fakeKeys = generateFakeResponseKeys(numberOfEmails);
        
        when(gmailApiMock.getEmails()).thenReturn(fakeEmails);
        
        Map<String, String> map = itemUnderTest.mapEmailsToKeys(fakeEmails,
        fakeKeys);
        
        assertTrue(map.keySet().containsAll(fakeEmails));
    }

    // sendTheEmails takes in the email/key map and sends out emails with the keys
    // * no idea how to test this one *
    @Test
    void testSendTheEmails() {
        //todo mock gmailsender
        Map<String, String> fakeEmailMap =  new HashMap<String, String>();
        fakeEmailMap.put("a@dnc.com","a bunch of fake html");
        fakeEmailMap.put("b@dnc.com","more fake html");

        itemUnderTest.sendTheEmails(fakeEmailMap);

        verify(mockGmailSender,
                times(fakeEmailMap.size())).sendEmail(any(String.class), any(String.class), any(String.class));

    }

}
