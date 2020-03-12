package com.objectcomputing.pulsesurvey.model;

import java.util.List;

public class SendSurveysCommand {

    String templateName;
    String percentOfEmails;
    List<String> emailAddresses;

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

    public List<String> getEmailAddresses() { return emailAddresses; }

    public void setEmailAddresses(List<String> emailAddresses) { this.emailAddresses = emailAddresses; }

    public SendSurveysCommand() {
    }

    public SendSurveysCommand(String templateName, String percentOfEmails) {
        this.templateName = templateName;
        this.percentOfEmails = percentOfEmails;
    }

}
