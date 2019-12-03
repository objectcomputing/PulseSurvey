package send.surveys;

import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;
import javax.inject.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
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
        // System.getenv("NAME_OF_YOUR_ENV_VARIABLE")
        List<String> emailAddresses = getRandomEmailAddresses(5);
        System.out.println("   EMAIL addresses: " + emailAddresses);
        List<String> keys = generateKeys(3);
        System.out.println("   KEYS: " + keys);
        return msg;
    }

    public List<String> getRandomEmailAddresses(int whatPercentOfTotal) {
        double totalAddresses = getTotalNumberOfAvailableEmailAddresses();
//        double totalAddresses = 170.0;   //change this later with a call to google groups api to get this number
        double percentToSend = (double)whatPercentOfTotal/100;
        long numberOfAddresses = Math.round(totalAddresses*percentToSend);
        List<String> emailAddresses = new ArrayList<String>();

        System.out.println("   whatPercentOfTotal: " + whatPercentOfTotal);
        System.out.println("   percentToSend: " + percentToSend);
        System.out.println("   totalAddresses: " + totalAddresses);
        System.out.println("   How many EMAIL addresses to send to: " + numberOfAddresses);

        emailAddresses.add("williamsh@objectcomputing.com");
        emailAddresses.add("kimberlinm@objectcomputing.com");
        emailAddresses.add("patilm@objectcomputing.com");
        return emailAddresses;
    }

    public List<String> generateKeys(int howManyKeys) {
        List<String> keys = new ArrayList<String>();

        for(int i = 0; i < howManyKeys; i++) {
            keys.add(UUID.randomUUID().toString());
        }

        return keys;
    }

    public int getTotalNumberOfAvailableEmailAddresses() {

        int howMany = 170;  //replace this with a call to google groups
        return howMany;
    }

    public Map<String, String> mapEmailsToKeys(List<String> emails, List<String> keys) {

        Map<String, String> map = new HashMap<String, String>();
        int numberOfPairs = Math.min(emails.size(), keys.size());

        for (String email:emails) {
            // add email and key to map
            map.put
        }

        return map;
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

