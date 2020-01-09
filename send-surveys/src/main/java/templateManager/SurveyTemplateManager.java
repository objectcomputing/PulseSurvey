package templateManager;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Singleton
public class SurveyTemplateManager {

    private final MustacheFactory factory;

    @Inject
    public SurveyTemplateManager(MustacheFactory factory) {
        this.factory = factory;
    }

    public Mustache getMustache(String mustacheFileName) {

        Mustache m = factory.compile(mustacheFileName);

        return m;
    }

    public String populateTemplate(String templateFileName, Map<String, Object> data)
            throws IOException {

        StringWriter writer = new StringWriter();
        getMustache(templateFileName).execute(writer, data).flush();

        return writer.toString();
    }

}
