package send.surveys;

import io.micronaut.core.annotation.*;

@Introspected
public class SendSurveys {

    private String name;

    public SendSurveys() {}

    public SendSurveys(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
