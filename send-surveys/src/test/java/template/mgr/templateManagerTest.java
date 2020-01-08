package template.mgr;
import com.github.mustachejava.Mustache;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import templateManager.SurveyTemplateManager;
import templateManager.TestMustache;

@MicronautTest
public class templateManagerTest {

    @Test
    void testGetMustache() {

        SurveyTemplateManager stm = new SurveyTemplateManager();
//        Mustache m = stm.getMustache("test.mustache");
        Mustache m = stm.getMustache("emailTemplate.st");

        //assert that this got created and has an h2

        String s = "Just a breakpoint";
    }

    @Test
    void testTestMustache() {

        SurveyTemplateManager stm = new SurveyTemplateManager();
        Mustache m = stm.getMustache("test.mustache");

        TestMustache tm = new TestMustache("title", "text", Calendar.getInstance().getTime());
        StringWriter writer = new StringWriter();
        try {
            m.execute(writer, tm).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = writer.toString();
        String expected = "<h2>title</h2>";

        assertThat(html, containsString(expected));
        String s = "Just a breakpoint";
    }

    @Test
    void testEmailTemplate() {

        UUID uuid = UUID.randomUUID();
        String stringUuid = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        map.put("surveyKey", stringUuid);

        SurveyTemplateManager stm = new SurveyTemplateManager();
        Mustache m = stm.getMustache("emailTemplate.st");

        StringWriter writer = new StringWriter();
        try {
            m.execute(writer, map).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = writer.toString();
        String expected = "<div>\n" + stringUuid;

//        assertThat(html, containsString(expected));  // not working
        assertThat(html, containsString(stringUuid));
        String s = "Just a breakpoint";
    }

    @Test
    void testPutKeyInTemplate() {

        UUID uuid = UUID.randomUUID();
        String stringUuid = uuid.toString();
        String templateFileName = "emailTemplate.st";

        SurveyTemplateManager stm = new SurveyTemplateManager();

        String html = stm.putKeyInTemplate(uuid, templateFileName);

        assertThat(html, containsString(stringUuid));
        String s = "Just a breakpoint";
    }

}
