package templateManager;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.inject.Singleton;

@Singleton
public class SurveyTemplateManager {

    public Mustache getMustache(String mustacheFileName){
        MustacheFactory mf = new DefaultMustacheFactory();
 //       Mustache m = mf.compile("test.mustache");
        Mustache m = mf.compile(mustacheFileName);
        return m;
    }

    // some meth to put stuff in the mustache

}
