package templateManager;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SurveyTemplateManager {

    public Mustache getMustache(String mustacheFileName){
        MustacheFactory mf = new DefaultMustacheFactory();
 //       Mustache m = mf.compile("test.mustache");
        Mustache m = mf.compile(mustacheFileName);
        return m;
    }

    // some meth to put stuff in the mustache
    public String putKeyInTemplate(UUID uuid) {

        String stringUuid = uuid.toString();
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

        return html;
    }

}
