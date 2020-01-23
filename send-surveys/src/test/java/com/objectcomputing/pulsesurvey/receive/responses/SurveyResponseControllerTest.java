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
    void testHappiness() {

        String currentEmotion = "happy";
        String surveyKey = "123";

        HttpResponse<ByteBuffer> response = httpClient
                .exchange(HttpRequest.GET(String.format("/happiness?currentEmotion=%s&surveyKey=%s", currentEmotion, surveyKey)))
                .blockingFirst();

        assertEquals(HttpStatus.OK, response.getStatus());
        response.equals("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

    }

}