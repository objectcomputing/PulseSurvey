package send.surveys;

import io.micronaut.function.executor.FunctionInitializer;
import model.ResponseKey;
import repositories.ResponseKeyRepository;
import io.micronaut.function.FunctionBean;

import javax.inject.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionBean("send-surveys")
public class SendSurveysFunction extends FunctionInitializer
        implements Supplier<SendSurveys> {
    private static final Logger LOG = LoggerFactory.getLogger(SendSurveysFunction.class);

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
    @Override
    public SendSurveys get() {
        SendSurveys msg = new SendSurveys();

        // to get aws environment vars:
        //  System.getenv("NAME_OF_YOUR_ENV_VARIABLE") // NOTE: getenv returns a string
        LOG.error("Reading env var System.getenv(PERCENT_OF_EMAILS): " + System.getenv("PERCENT_OF_EMAILS"));
        int percentOfEmailsToGet = Integer.parseInt(System.getenv("PERCENT_OF_EMAILS"));
        LOG.error("Reading env var percentOfEmailsToGet: " + percentOfEmailsToGet);
        msg.setName("I'm gonna send "+percentOfEmailsToGet+"% of surveys, eventually.");
        // can also get all of the env vars in a Map
        // see:  https://docs.oracle.com/javase/tutorial/essential/environment/env.html
//        int percentOfEmailsToGet = 9;  // will be set from env var
        LOG.info("Grabbing email addresses.");
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        LOG.info("Generating keys.");
        List<ResponseKey> keys = generateKeys(emailAddresses.size());
        LOG.info("Mapping emails to keys.");
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        LOG.info("   And Finally  - emailKeyMap: " + emailKeyMap);

        // send some emails
        return msg;
    }

    List<String> getRandomEmailAddresses(int percentOfEmailsNeeded) {
        double totalAddresses = getTotalNumberOfAvailableEmailAddresses();
        long numberOfAddressesRequested = (long) Math.ceil(totalAddresses * (double) percentOfEmailsNeeded / 100.0);
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

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String... args) throws IOException {
        SendSurveysFunction function = new SendSurveysFunction();
        function.run(args, (context) -> function.get());
    }
}

