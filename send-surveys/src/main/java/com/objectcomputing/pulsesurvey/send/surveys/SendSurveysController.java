package com.objectcomputing.pulsesurvey.send.surveys;

import io.micronaut.function.executor.FunctionInitializer;
import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import io.micronaut.function.FunctionBean;

import javax.inject.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@FunctionBean("send-surveys")
@Controller("/sendEmails")
public class SendSurveysController {
    private static final Logger LOG = LoggerFactory.getLogger(SendSurveysController.class);

    @Inject
    private ResponseKeyRepository responseKeyRepo;

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

    @Get("/{percentOfEmails}")
    public SendSurveys get(String percentOfEmails) {

        // to get aws environment vars:
        //  System.getenv("NAME_OF_YOUR_ENV_VARIABLE") // NOTE: getenv returns a string
        LOG.info("Reading percentOfEmails: " + percentOfEmails);
        int percentOfEmailsToGet = Integer.parseInt(percentOfEmails);
        LOG.info("Reading env var percentOfEmailsToGet: " + percentOfEmailsToGet);

        LOG.info("Grabbing email addresses.");
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        LOG.info("Generating keys.");
        List<ResponseKey> keys = generateKeys(emailAddresses.size());
        LOG.info("Mapping emails to keys.");
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        LOG.info("   And Finally  - emailKeyMap: " + emailKeyMap);

        // send some emails
        return new SendSurveys("Sent surveys: " + emailKeyMap.size());
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

    List<ResponseKey> generateKeys(int howManyKeys) {
        
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

    void sendTheEmails(List<String> emails, List<String> keys) {

        // call some google api with the list of emails to send them with a key for each

    }

}

