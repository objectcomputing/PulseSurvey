package com.objectcomputing.pulsesurvey.model;

public class Survey {

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

    public Survey() {
    }

    public Survey(String templateName, String percentOfEmails) {
        this.templateName = templateName;
        this.percentOfEmails = percentOfEmails;
    }

}
