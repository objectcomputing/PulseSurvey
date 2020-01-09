package templateManager;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SurveyTemplateManager {

    private final MustacheFactory factory;

    @Inject
    public SurveyTemplateManager(MustacheFactory factory) {
        this.factory = factory;
    }

    public Mustache getMustache(String mustacheFileName){
        Mustache m = factory.compile(mustacheFileName);
        return m;
    }

    // put stuff in the mustache template
    public String populateTemplate(String templateFileName, Map<String, Object> data) {

        Mustache m = getMustache(templateFileName);

        StringWriter writer = new StringWriter();
        try {
            m.execute(writer, data).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = writer.toString();

        return html;
    }

}
