package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.model.Response;
import com.objectcomputing.pulsesurvey.repositories.ResponseRepository;
import com.objectcomputing.pulsesurvey.send.surveys.SurveysControllerTest;
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

    ResponseRepository mockRepository = mock(ResponseRepository.class);

    private static final Logger LOG = LoggerFactory.getLogger(SurveysControllerTest.class);

    @BeforeEach
    void setupTest() {
        itemUnderTest.setResponseRepo(mockRepository);
        reset(mockRepository);
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
    void testHappinessAddResponse() {

        String currentEmotion = "happyTest";
        String surveyKey =       "12345678-9123-4567-abcd-123456789abc";
        String fakeResponseKey = "98765432-9876-9876-9876-987654321234";

        Response fakeResponse = new Response();
        fakeResponse.setResponseId(UUID.fromString(fakeResponseKey));
        fakeResponse.setResponseKey(UUID.fromString(surveyKey));
        fakeResponse.setSelected(currentEmotion);
        fakeResponse.setCreatedOn(LocalDateTime.of(2020, Month.JANUARY, 27, 1, 1));

        when(mockRepository.save(any())).thenReturn(fakeResponse);

        assertTrue(itemUnderTest.addResponse(currentEmotion, surveyKey));

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

        when(mockRepository.save(any())).thenReturn(fakeResponse);

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness?currentEmotion=%s&surveyKey=%s",
                        currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

}