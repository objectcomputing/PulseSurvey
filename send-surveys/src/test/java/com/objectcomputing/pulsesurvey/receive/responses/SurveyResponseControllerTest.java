package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.send.surveys.SurveysControllerTest;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SystemPropertyExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SystemPropertyExtension.class)
@MicronautTest
class SurveyResponseControllerTest {

    @Inject
    @Client("/")
    RxHttpClient httpClient;

    @Inject
    SurveyResponseController itemUnderTest;

    private static final Logger LOG = LoggerFactory.getLogger(SurveysControllerTest.class);

    @Test
    void testHappinessReceived() {

        String currentEmotion = "happy";
        String surveyKey = "123";

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness/received?currentEmotion=%s&surveyKey=%s", currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

    @Test
    void testHappinessValidateKey() {

        // mock db
        String currentEmotion = "happy";
        String surveyKey = "68fe8990-be83-49ec-b885-17cf1db78001";

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness?currentEmotion=%s&surveyKey=%s",
                        currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
//        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
//                " with a key of: " + surveyKey);

    }

    @Test
    void testHappiness() {

        // mock db
        String currentEmotion = "happy";
        String surveyKey = "4975741f-4464-4e49-9165-ddb0359d6b6e";

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness?currentEmotion=%s&surveyKey=%s",
                        currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

}