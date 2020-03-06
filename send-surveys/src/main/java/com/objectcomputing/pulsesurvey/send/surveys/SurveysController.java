package com.objectcomputing.pulsesurvey.send.surveys;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import com.objectcomputing.pulsesurvey.email.manager.GmailSender;
import com.objectcomputing.pulsesurvey.email.manager.GoogleDirectoryAccessor;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.model.SendSurveysCommand;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("/surveys")
public class SurveysController {
    private static final Logger LOG = LoggerFactory.getLogger(SurveysController.class);
    private List<String> users;
//    private List<User> users;

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    @Inject
    private SurveyTemplateManager templateManager;

    @Inject
    private GmailSender gmailSender;

    @Inject
    private GoogleDirectoryAccessor googleDirectoryAccessor;

    private void updateUserList() {
        try {
//            Directory directory = googleDirectoryAccessor.accessGoogleDirectory();
//            Users userList = directory.users().list().execute(); // After list .setDomain or setCustomer to get only users who matter
//            users = userList.getUsers();

            users = new ArrayList<String>();
            users.add("williamsh@objectcomputing.com");
            users.add("kimberlinm@objectcomputing.com");
            users.add("mckiernanc@objectcomputing.com");
            users.add("warnerj@objectcomputing.com");
            users.add("hannerj@objectcomputing.com");
            users.add("patilm@objectcomputing.com");
//        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
            users = null;
        }
    }

    public void setResponseKeyRepo(ResponseKeyRepository responseKeyRepository) {
        this.responseKeyRepo = responseKeyRepository;
    }

    public void setTemplateManager(SurveyTemplateManager surveyTemplateManager) {
        this.templateManager = surveyTemplateManager;
    }

    public void setGmailSender(GmailSender gmailSender) {
        this.gmailSender = gmailSender;
    }

    /* call GetRandomEmails(what percentage of current employees) ->
    GetEmail(Google) -> SelectRandom -> GenerateKeys -> Map<String Email, String KeyUUID>
    Map gets returned */

    @Post(value = "send")
    @Consumes(MediaType.APPLICATION_JSON)
    public SendSurveys sendEmails(@Body SendSurveysCommand sendSurveysCommand) {

        LOG.info("post survey.getTemplateName(): " + sendSurveysCommand.getTemplateName());
        LOG.info("post survey.getPercentOfEmails(): " + sendSurveysCommand.getPercentOfEmails());

        LOG.info("survey.percentOfEmails: " + sendSurveysCommand.getPercentOfEmails());
        int percentOfEmailsToGet = Integer.parseInt(sendSurveysCommand.getPercentOfEmails());
        LOG.info("percentOfEmailsToGet: " + percentOfEmailsToGet);

        LOG.info("Grabbing email addresses.");
        updateUserList();
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        LOG.info("Generating keys.");
        List<ResponseKey> keys = generateAndSaveKeys(emailAddresses.size());
        LOG.info("Mapping emails to keys.");
        Map<String, String> emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        LOG.info("And Finally  - emailKeyMap: " + emailKeyMap);

        Map<String, String> emailBodies = null;
        // populate the emails
        try {
             emailBodies = templateManager.populateEmails(sendSurveysCommand.getTemplateName(),
                                                          emailKeyMap);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        // send some emails
        sendTheEmails(emailBodies);

        return new SendSurveys("Sent surveys: " + emailKeyMap.size() +
                " for " + sendSurveysCommand.getPercentOfEmails() +
                " percent of total emails using " +
                sendSurveysCommand.getTemplateName() + " template");
    }

    List<String> getRandomEmailAddresses(int percentOfEmailsNeeded) {
        int totalAddresses = (int) Math.ceil(users.size() * (percentOfEmailsNeeded/100.0));
        LOG.info("totalAddresses: " + totalAddresses);

        Collections.shuffle(users);
        return users.subList(0,totalAddresses);
//        return users.subList(0, totalAddresses).stream()
//                .map(User::getPrimaryEmail).collect(Collectors.toList());
    }

    List<ResponseKey> generateAndSaveKeys(int howManyKeys) {

        List<ResponseKey> keys = new ArrayList<ResponseKey>();

        for (int i = 0; i < howManyKeys; i++) {
            keys.add(new ResponseKey());
        }
        LOG.info("Storing empty keys.");
        List<ResponseKey> returned = responseKeyRepo.saveAll(keys);
        LOG.info("Keys stored.");

        return returned;
    }

    Map<String, String> mapEmailsToKeys(List<String> emails, List<ResponseKey> keys) {

        Map<String, String> map = new HashMap<String, String>();
        int numberOfPairs = Math.min(emails.size(), keys.size());

        for (int i = 0; i < numberOfPairs; i++) {
            map.put(emails.get(i), keys.get(i).getResponseKey().toString());
        }

        return map;
    }

    void sendTheEmails(Map<String, String> emailAddressToBodiesMap) {
        // call some google api with the list of emails to send them with a key for each
        LOG.info("I'm sending the emails now");

        // GmailSender is currently not working
       // emailAddressToBodiesMap.forEach((email, body) ->
       // gmailSender.sendEmail("Feelings, Whoa, Whoa, Whoa, Feelings", email, body));
    }
}

