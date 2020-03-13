package com.objectcomputing.pulsesurvey.send.surveys;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.objectcomputing.pulsesurvey.email.manager.MailJetSender;
import com.objectcomputing.pulsesurvey.model.SendSurveysCommand;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;

import javax.inject.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/surveys")
public class SurveysController {
    private static final Logger LOG = LoggerFactory.getLogger(SurveysController.class);

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    @Inject
    private SurveyTemplateManager templateManager;

    @Inject
    private MailJetSender emailSender;

    public void setResponseKeyRepo(ResponseKeyRepository responseKeyRepository) {
        this.responseKeyRepo = responseKeyRepository;
    }

    public void setTemplateManager(SurveyTemplateManager surveyTemplateManager) {
        this.templateManager = surveyTemplateManager;
    }

    /* call GetRandomEmails(what percentage of current employees) ->
    GetEmail(Google) -> SelectRandom -> GenerateKeys -> Map<String Email, String KeyUUID>
    Map gets returned */

    @Post(value = "send")
    @Consumes(MediaType.APPLICATION_JSON)
    public SendSurveys sendEmails(@Body SendSurveysCommand sendSurveysCommand) {

        LOG.info("post survey.getTemplateName(): " + sendSurveysCommand.getTemplateName());
        LOG.info("post survey.getPercentOfEmails(): " + sendSurveysCommand.getPercentOfEmails());
        LOG.info("post survey.getEmailAddresses(): " + sendSurveysCommand.getEmailAddresses());
        List<String> emailAddresses = sendSurveysCommand.getEmailAddresses();

        LOG.info("survey.percentOfEmails: " + sendSurveysCommand.getPercentOfEmails());
        int percentOfEmailsToGet = Integer.parseInt(sendSurveysCommand.getPercentOfEmails());
        LOG.info("percentOfEmailsToGet: " + percentOfEmailsToGet);

        LOG.info("Grabbing email addresses.");
        emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet, emailAddresses);
        List<ResponseKey> keys = generateAndSaveKeys(emailAddresses.size());
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        LOG.info("And Finally  - emailKeyMap: " + emailKeyMap);

        Map<String, String> emailBodies = null;
        // populate the emails with the keys
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

    List<String> getRandomEmailAddresses(int percentOfEmailsNeeded, List<String> emailAddresses) {
        double totalAddresses = emailAddresses.size();
        LOG.info("totalAddresses: " + totalAddresses);
        long numberOfAddressesRequested = (long) Math.ceil(totalAddresses * (double) percentOfEmailsNeeded / 100.0);
        LOG.info("numberOfAddressesRequested: " + numberOfAddressesRequested);
        List<String> randomSubsetEmailAddresses = new ArrayList<String>();

        for (int i = 0; i < numberOfAddressesRequested; i++) {
            randomSubsetEmailAddresses.add(emailAddresses.get(i));
        }
        return randomSubsetEmailAddresses;
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

        LOG.info("I'm sending the emails now");

        try {
            emailSender.emailSender(emailAddressToBodiesMap);
        } catch (MailjetException e) {
            LOG.info("Mailjet exception: " + e.getMessage());
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            LOG.info("Mailjet socket timeout exception: " + e.getMessage());
            e.printStackTrace();
        }

    }

}

