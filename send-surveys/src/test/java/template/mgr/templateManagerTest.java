package template.mgr;
import com.github.mustachejava.Mustache;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        Mustache m = objectUnderTest.getMustache("emailTemplate.st");

        assertNotNull(m);
    }

    @Test
    void testEmailTemplate() {

        UUID uuid = UUID.randomUUID();
        String stringUuid = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        map.put("surveyKey", stringUuid);

        Mustache m = objectUnderTest.getMustache("emailTemplate.st");

        StringWriter writer = new StringWriter();
        try {
            m.execute(writer, map).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = writer.toString();
        String expected = "<div>\n" + stringUuid;

        assertThat(html, containsString(stringUuid));
        String s = "need a breakpoint";
    }

    @Test
    void testPutKeyInTemplate() {

        UUID uuid = UUID.randomUUID();
        String stringUuid = uuid.toString();
        String templateFileName = "emailTemplate.st";

        Map<String, Object> map = new HashMap<>();
        map.put("surveyKey", stringUuid);

        String html = objectUnderTest.populateTemplate(templateFileName, map);

        assertThat(html, containsString(stringUuid));
        String s = "need a breakpoint";
    }

}
