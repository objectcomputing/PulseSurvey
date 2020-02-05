package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.email.manager.GmailSender;
import com.objectcomputing.pulsesurvey.model.Response;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.repositories.ResponseRepository;
import com.objectcomputing.pulsesurvey.send.surveys.SurveysControllerTest;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SystemPropertyExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(SystemPropertyExtension.class)
@MicronautTest
class SurveyResponseControllerTest {

    @Inject
    @Client("/")
    RxHttpClient httpClient;

    @Inject
    SurveyResponseController itemUnderTest;

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    ResponseRepository mockResponseRepository = mock(ResponseRepository.class);

    ResponseKeyRepository mockResponseKeyRepository = mock(ResponseKeyRepository.class);

    SurveyTemplateManager mockTemplateManager = mock(SurveyTemplateManager.class);

    GmailSender mockGmailSender = mock(GmailSender.class);

    private static final Logger LOG = LoggerFactory.getLogger(SurveysControllerTest.class);

    @BeforeEach
    void setupTest() {
        itemUnderTest.setResponseRepo(mockResponseRepository);
        itemUnderTest.setResponseKeyRepo(mockResponseKeyRepository);
//        itemUnderTest.setGmailApi(gmailApiMock);
//        itemUnderTest.setTemplateManager(mockTemplateManager);
//        itemUnderTest.setGmailSender(mockGmailSender);
//        reset(gmailApiMock);
//        reset(mockTemplateManager);
        reset(mockResponseKeyRepository);
        reset(mockGmailSender);
        reset(mockResponseRepository);
    }

    @Test
    void testHappinessReceived() {

        String currentEmotion = "happyTest";
        String surveyKey = "123";

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness/received?currentEmotion=%s&surveyKey=%s", currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

    @Test
    void testValidateKey_validKey() {

        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";
        boolean fakeUsed = false;

        ResponseKey fakeResponseKeyObj = new ResponseKey();
        fakeResponseKeyObj.setResponseKey(UUID.fromString(fakeResponseKey));
        fakeResponseKeyObj.setUsed(fakeUsed);
        fakeResponseKeyObj.setIssuedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockResponseKeyRepository.findById(any()))
                .thenReturn(java.util.Optional.of(fakeResponseKeyObj));

        assertTrue(itemUnderTest.validateKey(surveyKey));

    }

    @Test
    void testValidateKey_invalidKey() {

        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";
        boolean fakeUsed = true;

        ResponseKey fakeResponseKeyObj = new ResponseKey();
        fakeResponseKeyObj.setResponseKey(UUID.fromString(fakeResponseKey));
        fakeResponseKeyObj.setUsed(fakeUsed);
        fakeResponseKeyObj.setIssuedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockResponseKeyRepository.findById(any()))
                .thenReturn(java.util.Optional.of(fakeResponseKeyObj));

        assertFalse(itemUnderTest.validateKey(surveyKey));

    }

    @Test
    void testValidateKey_absentKey() {

        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";
        boolean fakeUsed = true;

        ResponseKey fakeResponseKeyObj = new ResponseKey();
        fakeResponseKeyObj.setResponseKey(UUID.fromString(fakeResponseKey));
        fakeResponseKeyObj.setUsed(fakeUsed);
        fakeResponseKeyObj.setIssuedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockResponseKeyRepository.findById(any()))
                .thenReturn(java.util.Optional.empty());

        assertFalse(itemUnderTest.validateKey(surveyKey));

    }

    @Test
    void testHappinessAddResponse() {

        String currentEmotion = "happyTest";
        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";

        Response fakeResponse = new Response();
        fakeResponse.setResponseId(UUID.fromString(fakeResponseKey));
        fakeResponse.setResponseKey(UUID.fromString(surveyKey));
        fakeResponse.setSelected(currentEmotion);
        fakeResponse.setCreatedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockResponseRepository.save(any())).thenReturn(fakeResponse);

        assertTrue(itemUnderTest.addResponse(currentEmotion, surveyKey));

    }

    @Test
    void testMarkKeyAsUsed() {

        ResponseKey key = new ResponseKey();

        // insert key into real responsekeys table
        ResponseKey returnedKey = responseKeyRepo.save(key);
        //run test against real table


        itemUnderTest.markKeyAsUsed(returnedKey.getResponseKey().toString());

    }

    @Test
    void testHappiness() {

        String currentEmotion = "happyTest";
        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";

        Response fakeResponse = new Response();
        fakeResponse.setResponseId(UUID.fromString(fakeResponseKey));
        fakeResponse.setResponseKey(UUID.fromString(surveyKey));
        fakeResponse.setSelected(currentEmotion);
        fakeResponse.setCreatedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockResponseRepository.save(any())).thenReturn(fakeResponse);

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness?currentEmotion=%s&surveyKey=%s",
                        currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

}