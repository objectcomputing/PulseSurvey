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

public class TestMailJetSend {

    public static void main(String[] args) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(System.getenv("MJ_APIKEY_PUBLIC"), System.getenv("MJ_APIKEY_PRIVATE"), new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "kimberlinm@objectcomputing.com")
                                        .put("Name", "Michael"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "schindlerj@objectcomputing.com")
                                                .put("Name", "Holly")))
                                .put(Emailv31.Message.SUBJECT, "Feelings, Whoa, Whoa, Whoa, Feelings")
                                .put(Emailv31.Message.TEXTPART, "How is your work day?")
                                .put(Emailv31.Message.HTMLPART, "<a href='http://localhost:8080/happiness?currentEmotion=happy&surveyKey=358d819c-0a8f-4b9c-b566-88e7e534dc81'>Happy</a>")
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
