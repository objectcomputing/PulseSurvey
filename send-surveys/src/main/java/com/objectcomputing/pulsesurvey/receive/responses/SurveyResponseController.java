package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.model.Response;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.repositories.ResponseRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@Controller("/happiness")
public class SurveyResponseController {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyResponseController.class);

    @Inject
    private ResponseRepository responseRepo;

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    public void setResponseRepo(ResponseRepository responseRepository) {
        this.responseRepo = responseRepository;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/received")
    HttpResponse<String> happinessReceived(String currentEmotion, String surveyKey) {

        LOG.info("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        return HttpResponse.ok("Hello, your current emotion is " + currentEmotion + "!" +
                               " with a key of: " + surveyKey);

    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    HttpResponse<String> happiness(String currentEmotion, String surveyKey) {

        LOG.info("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        boolean goodKey = false;
        goodKey = responseKeyRepo.existsById(UUID.fromString(surveyKey));

        LOG.info("happiness - key is valid? " + goodKey);

        // if yes - store happiness (and comments(?))
        if (goodKey) {
            boolean responseAdded = addResponse(currentEmotion, surveyKey);

            // (maybe create usercomments row and fill it in?)

            removeKey(surveyKey);  // currently problematic

            sendThankYou("someemailaddressfromsomewhere");

        } else {
            LOG.warn("This key is not valid ");
        }

        return HttpResponse.ok("Hello, your current emotion is " + currentEmotion + "!" +
                               " with a key of: " + surveyKey);

    }

    boolean addResponse(String currentEmotion, String surveyKey) {

        boolean responseAdded = false;
        Response response = new Response();

        response.setResponseKey(UUID.fromString(surveyKey));
        response.setSelected(currentEmotion);
        response = responseRepo.save(response);

        LOG.info("Adding response " + currentEmotion + "  ");

//        if (response.getCreatedOn() != null) responseAdded = true;
        if (response.getResponseId() != null) responseAdded = true;

        return responseAdded;
    }

    private void validateKey(String surveyKey) {

        LOG.info("Validating key" + surveyKey);

        Optional<Response> response = responseRepo.findById(UUID.fromString(surveyKey));
        LOG.info("response: " + response.get());
        boolean keyIsInDb = responseRepo.existsById(UUID.fromString(surveyKey));
        LOG.info("key is valid? " + keyIsInDb);

    }

    private void removeKey(String surveyKey) {

        LOG.info("Removing key: " + surveyKey + " from responsekeys");

        // can't remove this because response has it as a foreign key

   //     responseKeyRepo.deleteById(UUID.fromString(surveyKey));

    }

    private void sendThankYou(String emailAddress) {

        LOG.info("Sending thank you to " + emailAddress);


    }

}