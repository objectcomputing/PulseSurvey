package com.objectcomputing.pulsesurvey.template.manager;
import com.github.mustachejava.Mustache;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MicronautTest
public class templateManagerTest {

    private static final Logger LOG = LoggerFactory.getLogger(templateManagerTest.class);

    @Inject
    SurveyTemplateManager objectUnderTest;

    @Test
    void testGetMustache() {

        Mustache m = objectUnderTest.getMustache("emailTemplate");

        assertNotNull(m);
    }

    @Test
    void testPopulateTemplate() {

        String stringUuid = UUID.randomUUID().toString();
        String templateFileName = "emailTemplate";
        Map<String, String> emailKeyMap;

        Map<String, Object> map = new HashMap<>();
        map.put("surveyKey", stringUuid);

        String html = null;
        try {
            html = objectUnderTest.populateTemplate(templateFileName, map);
        } catch (IOException e) {
            LOG.error("IOException for: "+ templateFileName + " " + e.getMessage());
        }

        assertThat(html, containsString(stringUuid));
    }

    @Test
    void testPopulateEmails_single() {

        String templateFileName = "emailTemplate";
        Map<String, String> emailKeyMap = new HashMap<>();
        Map<String, String> emailBodies = new HashMap<>();

        emailKeyMap.put("a@dnc.com", "12345678");

        try {
            emailBodies = objectUnderTest.populateEmails(templateFileName, emailKeyMap);
        } catch (IOException e) {
            LOG.error("IOException for: "+ templateFileName + " " + e.getMessage());
            e.printStackTrace();
        }

        LOG.error("emailBodies.get(\"a@dnc.com\"): "+ emailBodies.get("a@dnc.com"));

        assertNotNull(emailBodies);
        assertThat(emailBodies.get("a@dnc.com"), containsString("12345678"));
    }


    @Test
    void testPopulateEmails_multiple() {

        String templateFileName = "emailTemplate";
        Map<String, String> emailKeyMap = new HashMap<>();
        Map<String, String> emailBodies = new HashMap<>();

        emailKeyMap.put("a@dnc.com", "12345678");
        emailKeyMap.put("b@dnc.com", "987654321");

        try {
            emailBodies = objectUnderTest.populateEmails(templateFileName, emailKeyMap);
        } catch (IOException e) {
            LOG.error("IOException for: "+ templateFileName + " " + e.getMessage());
            e.printStackTrace();
        }

        LOG.error("emailBodies.get(\"a@dnc.com\"): "+ emailBodies.get("a@dnc.com"));
        LOG.error("emailBodies.get(\"b@dnc.com\"): "+ emailBodies.get("b@dnc.com"));

        assertNotNull(emailBodies);
        assertThat(emailBodies.get("a@dnc.com"), containsString("12345678"));
        assertThat(emailBodies.get("b@dnc.com"), containsString("987654321"));
    }

}
