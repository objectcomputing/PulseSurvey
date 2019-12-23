package send.surveys;

import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;

import javax.inject.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionBean("send-surveys")
public class SendSurveysFunction extends FunctionInitializer
        implements Supplier<SendSurveys> {
    private static final Logger LOG = LoggerFactory.getLogger(SendSurveysFunction.class);

    class GmailApi {
        public List<String> getEmails() {
            List<String> emailAddresses = new ArrayList<>();
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
        List<String> emailAddresses = getRandomEmailAddresses(percentOfEmailsToGet);
        List<String> keys = generateKeys(emailAddresses.size());
        Map<String, String> emailKeyMap = new HashMap<String, String>();
        emailKeyMap = mapEmailsToKeys(emailAddresses, keys);
        LOG.info("   And Finally  - emailKeyMap: " + emailKeyMap);

        // store keys in database - aws rds using postgres using db.t2.micro
        // put keys in db
        // a key will need to be sent in each email
        storeKeysInDb(keys);
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

    List<String> generateKeys(int howManyKeys) {
        List<String> keys = new ArrayList<String>();

        for (int i = 0; i < howManyKeys; i++) {
            keys.add(UUID.randomUUID().toString());
        }

        return keys;
    }

    int getTotalNumberOfAvailableEmailAddresses() {
        return gmail.getEmails().size();
    }

    Map<String, String> mapEmailsToKeys(List<String> emails, List<String> keys) {

        Map<String, String> map = new HashMap<String, String>();
        int numberOfPairs = Math.min(emails.size(), keys.size());

        for (int i = 0; i < numberOfPairs; i++) {
            map.put(emails.get(i), keys.get(i));
        }

        return map;
    }

    Connection storeKeysInDb(List<String> keys) {

        // connect to db
        // store keys in keys table with time stamp issuedOn field in keys table

        Connection connection = null;
        String url = "jdbc:postgresql://pulsesurveydb.ca4gwn3eiebi.us-east-1.rds.amazonaws.com:5432/pulsesurveydb";

        String userName = "pulsesurvey";
        String password = "surveyadmin321";
        String dbName = "pulsesurveydb";
 //       String driver = "com.mysql.jdbc.Driver";
        try {
            connection = DriverManager.getConnection(url, userName, password);
            LOG.error("Database connection success! " );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
        Following is a guide on Working with DB Security Groups:
        http://docs.amazonwebservices.com/AmazonRDS/latest/UserGuide/USER_WorkingWithSecurityGroups.html

        Here is some sample code for a JDBC connector for mysql:
        String url = "jdbc:mysql://dbname.test.us-east-1.rds.amazonaws.com:3306/";
        pulsesurveydb.ca4gwn3eiebi.us-east-1.rds.amazonaws.com:5432
        String userName = "your_user_name";
        String password = "your_password";
        String dbName = "your_db_name";
        String driver = "com.mysql.jdbc.Driver";
        Connection connection = DriverManager.getConnection(url + dbName, userName, password);

        The Amazon RDS API Refence is available here:
        http://docs.amazonwebservices.com/AmazonRDS/latest/APIReference/Welcome.html?r=6650
*/
        return connection;
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

