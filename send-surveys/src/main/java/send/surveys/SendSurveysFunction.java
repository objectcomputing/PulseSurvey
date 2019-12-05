package send.surveys;

import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;
import javax.inject.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

@FunctionBean("send-surveys")
public class SendSurveysFunction extends FunctionInitializer
        implements Supplier<SendSurveys> {

    /* call GetRandomEmails(what percentage of current employees) ->
    GetEmail(Google) -> SelectRandom -> GenerateKeys -> Map<String Email, String KeyUUID>
    Map gets returned */
    @Override
    public SendSurveys get() {
        SendSurveys msg = new SendSurveys();
        msg.setName("I'm gonna send surveys, eventually.");
        // to get aws environment vars:
        //  System.getenv("NAME_OF_YOUR_ENV_VARIABLE") // NOTE: getenv returns a string
        //  int percentOfEmailsToGet = System.getenv("PercentOfEmailsToGet");
        // can also get all of the env vars in a Map
        // see:  https://docs.oracle.com/javase/tutorial/essential/environment/env.html

        int percentOfEmailsToGet = 9;  // will be set from env var
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        List<String> keys = generateKeys(emailAddresses.size());
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        System.out.println("   And Finally  - emailKeyMap: " + emailKeyMap);
        // store keys in database - aws rds using postgres using db.t2.micro
        // send some emails
        return msg;
    }

     List<String> getRandomEmailAddresses(int whatPercentOfTotal) {
        double totalAddresses = getTotalNumberOfAvailableEmailAddresses();
        long numberOfAddresses = (long)Math.ceil(totalAddresses*(double)whatPercentOfTotal/100.0);
        List<String> emailAddresses = new ArrayList<String>();

        // later - get x random email addresses from google
        emailAddresses.add("williamsh@objectcomputing.com");
        emailAddresses.add("kimberlinm@objectcomputing.com");
        emailAddresses.add("patilm@objectcomputing.com");
        return emailAddresses;
    }

     List<String> generateKeys(int howManyKeys) {
        List<String> keys = new ArrayList<String>();

        for(int i = 0; i < howManyKeys; i++) {
            keys.add(UUID.randomUUID().toString());
        }

        return keys;
    }

     int getTotalNumberOfAvailableEmailAddresses() {

        int howMany = 170;  //replace this with a call to google groups
        return howMany;
    }

     Map<String, String> mapEmailsToKeys(List<String> emails, List<String> keys) {

        Map<String, String> map = new HashMap<String, String>();
        int numberOfPairs = Math.min(emails.size(), keys.size());

        for (int i = 0; i < numberOfPairs; i++) {
            // add email and key to map
            map.put(emails.get(i), keys.get(i));
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
    public static void main(String...args) throws IOException {
        SendSurveysFunction function = new SendSurveysFunction();
        function.run(args, (context)-> function.get());
    }    
}

