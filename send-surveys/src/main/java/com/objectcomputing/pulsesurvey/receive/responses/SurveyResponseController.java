package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.model.Response;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.model.UserComments;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.repositories.ResponseRepository;
import com.objectcomputing.pulsesurvey.repositories.UserCommentsRepository;
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
import java.util.List;
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

        LOG.info("Hello, I have received your emotion of: " + currentEmotion + "!" +
                " with a key of: " + surveyKey);

        return HttpResponse.ok("Hello, I have received your emotion of: " + currentEmotion + "!" +
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
                    LOG.info("redirecting to /happiness/comment");
                    return HttpResponse.temporaryRedirect(new URI("/happiness/comment?surveyKey="+surveyKey));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    LOG.error("unable to redirect to /happiness/comment " + e.getMessage());
                }
            }
        } else {
            LOG.warn("This key is not valid: " + surveyKey);
        }

        return HttpResponse.ok("This key has already been used.");
    }

    @Get("comment")
    @View("comment")
    public HttpResponse displayComments(String surveyKey) {

        // check to see if there is already a comment for this key
        LOG.info("in /comment. surveyKey = " + surveyKey);
        return HttpResponse.ok(CollectionUtils.mapOf("surveyKey", surveyKey));
    }

    @Post("userComments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @View("thankyou")
    public HttpResponse sendThankYouWithCommentBlock (String userComments, String surveyKey) {

        LOG.info("The user has commented: " + userComments);
        LOG.info("With surveyKey: " + surveyKey);
        // put comment into the db using the survey key
        // first check to see if there is currently a comment
        boolean alreadyIsComment = checkForComment(surveyKey);
        if (!alreadyIsComment) {
            saveUserComment(surveyKey, userComments);
            return HttpResponse.ok();
        } else {
            return HttpResponse.ok("This key has already been used.");
        }
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

    private boolean checkForComment(String surveyKey) {

        LOG.info("Validating comment " + surveyKey);
        List<UserComments> userComments = userCommentsRepo.findComments(surveyKey);

        boolean isPresent = false;
        isPresent = !userComments.isEmpty();
        return isPresent;

    }

    private void saveUserComment(String surveyKey, String commentText) {

        UserComments userComments = new UserComments();
        userComments.setCommentText(commentText);
        userComments.setResponseKey(UUID.fromString(surveyKey));
        userComments = userCommentsRepo.save(userComments);
    }

    ResponseKey markKeyAsUsed(String surveyKey) {

        LOG.info("Marking key as used: " + surveyKey + " from responsekeys");

        AtomicReference<ResponseKey> returnedResponseKey = new AtomicReference<>(new ResponseKey());
        Optional<ResponseKey> responseKey = responseKeyRepo.findById(UUID.fromString(surveyKey));

        responseKey.ifPresent(responseKeyToSave -> {
            responseKeyToSave.setUsed(true);
            returnedResponseKey.set(responseKeyRepo.update(responseKeyToSave));
        });
        return returnedResponseKey.get();
    }

}