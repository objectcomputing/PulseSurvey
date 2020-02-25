package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.model.Response;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.model.UserComments;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.repositories.ResponseRepository;
import com.objectcomputing.pulsesurvey.repositories.UserCommentsRepository;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Controller("/happiness")
public class SurveyResponseController {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyResponseController.class);

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    @Inject
    private ResponseRepository responseRepo;

    @Inject
    private UserCommentsRepository userCommentsRepo;

    @Inject
    private SurveyTemplateManager templateManager;

    public void setResponseKeyRepo(ResponseKeyRepository responseKeyRepository) {
        this.responseKeyRepo = responseKeyRepository;
    }

    public void setResponseRepo(ResponseRepository responseRepository) {
        this.responseRepo = responseRepository;
    }

    public void setUserCommentsRepo(UserCommentsRepository userCommentsRepository) {
        this.userCommentsRepo = userCommentsRepository;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/received")
    HttpResponse<String> happinessReceived(String currentEmotion, String surveyKey) {

        LOG.info("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        return HttpResponse.ok("Hello, your current emotion is " + currentEmotion + "!" +
                               " with a key of: " + surveyKey);
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    HttpResponse<String> happiness(String currentEmotion, String surveyKey) {

        LOG.info("Hello, your current emotion is " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        boolean validKey = false;
        validKey = validateKey(surveyKey);

        LOG.info("happiness - key is valid? " + validKey);

        // if yes - store happiness and comments
        if (validKey) {
            boolean responseAdded = saveResponse(currentEmotion, surveyKey);

            if (responseAdded) {
                markKeyAsUsed(surveyKey);

                try {
                    HttpResponse.redirect(new URI("/happiness/comment?surveyKey="+surveyKey));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    LOG.error("unable to redirect to /happiness/commentBlock " + e.getMessage());
                }
            }
        } else {
            LOG.warn("This key is not valid: " + surveyKey);
        }

        return HttpResponse.ok("Hello, your current emotion is " + currentEmotion + "!" +
                               " with a key of: " + surveyKey);
    }

    @Get("comment")
    @View("comment")
    public HttpResponse displayComments(String surveyKey) {

        LOG.info("redirect to comment surveyKey = " + surveyKey);
        return HttpResponse.ok(CollectionUtils.mapOf("surveyKey", surveyKey));
    }

    @Post("userComments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @View("thankyou")
    public HttpResponse sendThankYouWithCommentBlock
            (@Value("userComments") String userComments,
             @Value("surveyKey") String surveyKey) {

        LOG.info("The user has commented: " + userComments);
        LOG.info("With surveyKey: " + surveyKey);
        // put comment into the db using the survey key

        return HttpResponse.ok();
    }

    @Get("thanks")
    @View("thankyou")
    public HttpResponse sendThankYou() {

        LOG.info("Sending final thank you web page " );
        return HttpResponse.ok();

    }

    boolean validateKey(String surveyKey) {

        LOG.info("Validating key " + surveyKey);
        Optional<ResponseKey> responseKey = responseKeyRepo.findById(UUID.fromString(surveyKey));
        AtomicBoolean isValid = new AtomicBoolean(false);

        responseKey.ifPresent(key -> {
            isValid.set(!key.isUsed());
        });

        LOG.debug("Survey Key " + surveyKey + (isValid.get()?" is used":" is not used"));

        LOG.info("key is valid? " + isValid);

        return isValid.get();
    }

    boolean saveResponse(String currentEmotion, String surveyKey) {

        boolean responseAdded = false;
        Response response = new Response();

        response.setResponseKey(UUID.fromString(surveyKey));
        response.setSelected(currentEmotion);
        response = responseRepo.save(response);

        LOG.info("Adding response " + currentEmotion + "  ");

        if (response.getResponseId() != null) responseAdded = true;

        return responseAdded;
    }

    private void addUserComments(String surveyKey) {

        UserComments userComments = new UserComments();
 //       userComments.setCommentText();
    }

    ResponseKey markKeyAsUsed(String surveyKey) {

        LOG.info("Marking key as used: " + surveyKey + " from responsekeys");

        AtomicReference<ResponseKey> returnedResponseKey = new AtomicReference<>(new ResponseKey());
        Optional<ResponseKey> responseKey = responseKeyRepo.findById(UUID.fromString(surveyKey));

        responseKey.ifPresent(responseKeyToSave -> {
            responseKeyToSave.setUsed(true);
 // this next line is blowing up - micronaut data issue
            returnedResponseKey.set(responseKeyRepo.update(responseKeyToSave));
        });
        return returnedResponseKey.get();
    }

}