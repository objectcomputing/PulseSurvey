package com.objectcomputing.pulsesurvey.send.surveys;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailJetSender {
    private static final Logger LOG = LoggerFactory.getLogger(SurveysController.class);

    /**
     * This call sends a message to the given recipient with attachment.
     */
    public static void main(String[] args) throws MailjetException, MailjetSocketTimeoutException {
//    public void emailSender() throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(System.getenv("MJ_APIKEY_PUBLIC"), System.getenv("MJ_APIKEY_PRIVATE"), new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "kimberlinm@objectcomputing.com")
                                        .put("Name", "Michael Kimberlin"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "williamsh@objectcomputing.com")
                                                .put("Name", "Holly Williams")))
                                .put(Emailv31.Message.SUBJECT, "Feelings, Whoa, Whoa, Whoa, Feelings 1:44")
                                .put(Emailv31.Message.TEXTPART, "How is your work day? MailJetSender wants to know")
                                .put(Emailv31.Message.HTMLPART, "<h3>How is your work day? MailJetSender wants to know</h3><br /><a href='http://localhost:8080/happiness?currentEmotion=happy&surveyKey=358d819c-0a8f-4b9c-b566-88e7e534dc81'>Happy</a>")
                                .put(Emailv31.Message.ATTACHMENTS, new JSONArray()
                                        .put(new JSONObject()
                                                .put("ContentType", "text/plain")
                                                .put("Filename", "test.txt")
                                                .put("Base64Content", "VGhpcyBpcyB5b3VyIGF0dGFjaGVkIGZpbGUhISEK")))));
        response = client.post(request);
        LOG.info("Mailjet response status: " + response.getStatus());
        LOG.info("Mailjet response data: " + response.getData());
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
