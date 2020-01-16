package com.objectcomputing.pulsesurvey.model;

public class SendSurveysCommand {

    String templateName;
    String percentOfEmails;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPercentOfEmails() {
        return percentOfEmails;
    }

    public void setPercentOfEmails(String percentOfEmails) {
        this.percentOfEmails = percentOfEmails;
    }

    public SendSurveysCommand() {
    }

    public SendSurveysCommand(String templateName, String percentOfEmails) {
        this.templateName = templateName;
        this.percentOfEmails = percentOfEmails;
    }

}
