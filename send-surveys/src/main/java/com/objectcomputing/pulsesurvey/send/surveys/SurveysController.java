package com.objectcomputing.pulsesurvey.send.surveys;

import com.objectcomputing.pulsesurvey.email.manager.GmailSender;
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

import io.micronaut.http.annotation.Body;
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
    private GmailSender gmailSender;

    class GmailApi {
        public List<String> getEmails() {
            List<String> emailAddresses = new ArrayList<>();
            emailAddresses.add("a@oci.com");
            emailAddresses.add("b@oci.com");
            emailAddresses.add("c@oci.com");
            emailAddresses.add("d@oci.com");
            emailAddresses.add("e@oci.com");
            return emailAddresses;
        }
    }

    GmailApi gmail = new GmailApi();

    public void setGmailApi(GmailApi api) {
        this.gmail = api;
    }

    /* call GetRandomEmails(what percentage of current employees) ->
    GetEmail(Google) -> SelectRandom -> GenerateKeys -> Map<String Email, String KeyUUID>
    Map gets returned */

    @Post(value = "send")
    public SendSurveys sendEmails(@Body SendSurveysCommand sendSurveysCommand) {

        LOG.info("post survey.getTemplateName(): " + sendSurveysCommand.getTemplateName());
        LOG.info("post survey.getPercentOfEmails(): " + sendSurveysCommand.getPercentOfEmails());

        // to get aws environment vars:
        //  System.getenv("NAME_OF_YOUR_ENV_VARIABLE") // NOTE: getenv returns a string
        LOG.info("survey.percentOfEmails: " + sendSurveysCommand.getPercentOfEmails());
        int percentOfEmailsToGet = Integer.parseInt(sendSurveysCommand.getPercentOfEmails());
        LOG.info("percentOfEmailsToGet: " + percentOfEmailsToGet);

        LOG.info("Grabbing email addresses.");
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        LOG.info("Generating keys.");
        List<ResponseKey> keys = generateAndSaveKeys(emailAddresses.size());
        LOG.info("Mapping emails to keys.");
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
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
        double totalAddresses = getTotalNumberOfAvailableEmailAddresses();
        LOG.info("totalAddresses: " + totalAddresses);
        long numberOfAddressesRequested = (long) Math.ceil(totalAddresses * (double) percentOfEmailsNeeded / 100.0);
        LOG.info("numberOfAddressesRequested: " + numberOfAddressesRequested);
        List<String> emailAddresses = new ArrayList<String>();
        List<String> randomSubsetEmailAddresses = new ArrayList<String>();

        emailAddresses = gmail.getEmails();
        // get random sampling of emails
        // later - get x random email addresses from google
        for (int i = 0; i < numberOfAddressesRequested; i++) {
            randomSubsetEmailAddresses.add(emailAddresses.get(i));
        }
        //      return emailAddresses;
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

    int getTotalNumberOfAvailableEmailAddresses() {
        return gmail.getEmails().size();
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

        emailAddressToBodiesMap.forEach((address, body) ->
                gmailSender.sendEmail("Feelings, Whoa, Whoa, Whoa, Feelings", address, body)
                );

    }

}

