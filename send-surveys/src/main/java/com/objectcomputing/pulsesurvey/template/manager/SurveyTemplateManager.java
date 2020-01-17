package com.objectcomputing.pulsesurvey.template.manager;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SurveyTemplateManager {

    private final MustacheFactory factory;

    @Inject
    public SurveyTemplateManager(MustacheFactory factory) {
        this.factory = factory;
    }

    public Mustache getMustache(String mustacheFileName) {

        return factory.compile(mustacheFileName + ".mustache");
    }

    public Map<String, String> populateEmails(String templateFileName,
                                              Map<String, String> emailKeyMap)
            throws IOException {

        // i need:
        // template name,
        // map of emails to keys,
        // map of template variables and values for populateTemplate - make this one

        Map<String, String> emailBodies = new HashMap<>();

        // loop to populate each email and add that to the return map
        int numberOfEmails = emailKeyMap.size();

        for (Map.Entry<String, String> entry : emailKeyMap.entrySet()) {
            Map<String, Object> templateVariableValueMap = new HashMap<>();
            templateVariableValueMap.put("surveyKey", entry.getValue());
            emailBodies.put(entry.getKey(), populateTemplate(templateFileName, templateVariableValueMap));
        }



    //    emailBodies.put(emailaddress, populateTemplate(templateFileName, templateVariableValueMap));

        return emailBodies;  //email addresses to html with keys
    }

    public String populateTemplate(String templateFileName, Object data)
            throws IOException {

        StringWriter writer = new StringWriter();
        getMustache(templateFileName).execute(writer, data).flush();

        return writer.toString();
    }

}
