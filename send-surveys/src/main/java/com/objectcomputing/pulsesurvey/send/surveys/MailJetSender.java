package com.objectcomputing.pulsesurvey.send.surveys;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.mustachejava.DefaultMustacheFactory;
import com.objectcomputing.pulsesurvey.template.manager.SurveyTemplateManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailJetSender {

    private static final Logger LOG = LoggerFactory.getLogger(SurveysController.class);

    /**
     * This call sends a message to the given recipient with attachment.
     * @param emailAddressToBodiesMap
     */
    // emailAddressToBodiesMap is email, address, email body
    public void emailSender(Map<String, String> emailAddressToBodiesMap) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        client = new MailjetClient(System.getenv("MJ_APIKEY_PUBLIC"), System.getenv("MJ_APIKEY_PRIVATE"), new ClientOptions("v3.1"));

        Set<String> keys = emailAddressToBodiesMap.keySet();
        for(String key: keys) {
            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", "kimberlinm@objectcomputing.com")
                                            .put("Name", "Michael Kimberlin"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", key)))
                                    .put(Emailv31.Message.SUBJECT, "Feelings, Whoa, Whoa, Whoa, Feelings")
                                    .put(Emailv31.Message.HTMLPART, emailAddressToBodiesMap.get(key))));
            response = client.post(request);
            LOG.info("Mailjet response status: " + response.getStatus());
            LOG.info("Mailjet response data: " + response.getData());
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        }
    }
}
