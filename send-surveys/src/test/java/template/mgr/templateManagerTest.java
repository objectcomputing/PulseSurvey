package template.mgr;
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
import templateManager.SurveyTemplateManager;

@MicronautTest
public class templateManagerTest {

    @Inject
    SurveyTemplateManager objectUnderTest;

    @Test
    void testGetMustache() {

        Mustache m = objectUnderTest.getMustache("emailTemplate.mustache");

        assertNotNull(m);
    }

    @Test
    void testPopulateTemplate() {

        String stringUuid = UUID.randomUUID().toString();
        String templateFileName = "emailTemplate.mustache";

        Map<String, Object> map = new HashMap<>();
        map.put("surveyKey", stringUuid);

        String html = null;
        try {
            html = objectUnderTest.populateTemplate(templateFileName, map);
        } catch (IOException e) {
            e.printStackTrace();  //todo log this
        }

        assertThat(html, containsString(stringUuid));
    }

}
