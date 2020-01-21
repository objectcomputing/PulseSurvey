package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.send.surveys.SurveysController;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/happiness")
public class SurveyResponseController {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyResponseController.class);

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    HttpResponse<String> happiness(String currentEmotion, String surveyKey) {

        LOG.info("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        return HttpResponse.ok("Hello, your current emotion is " + currentEmotion + "!" +
                               " with a key of: " + surveyKey);

    }

}