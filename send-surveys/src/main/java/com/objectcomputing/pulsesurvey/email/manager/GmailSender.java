package com.objectcomputing.pulsesurvey.email.manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import io.micronaut.context.annotation.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

@Singleton
public class GmailSender {

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private final GoogleAuthenticator authenticator;
    private final String applicationName;
    private static final Logger LOG = LoggerFactory.getLogger(GmailSender.class);

    @Inject
    GmailSender(
            @Property(name = "oci-google-drive.application.name") String applicationName,
            GoogleAuthenticator authenticator) throws GeneralSecurityException, IOException {
        this.applicationName = applicationName;
        this.authenticator = authenticator;
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.jsonFactory = JacksonFactory.getDefaultInstance();
    }

    Gmail getService() throws IOException {
        return new Gmail.Builder(httpTransport, jsonFactory, authenticator.setupCredentials())
                .setApplicationName(applicationName)
                .build();
    }

    public void sendEmail(String subject, String emailAddress, String emailMessage) {
        try {
            Gmail emailService = getService();
            LOG.info("Email Address: " + emailAddress);
            LOG.info("Email body: " + emailMessage);

            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage emailContent = new MimeMessage(session);
            emailContent.setFrom("kimberlinm@objectcomputing.com");
            emailContent.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress("williamsh@objectcomputing.com"));
            emailContent.setSubject(subject);
            emailContent.setText(emailMessage);
/*.setContent(html code)  will have to be constructed from template */
            Message message = createMessageWithEmail(emailContent);
            message = emailService.users().messages().send("kimberlinm@objectcomputing.com", message).execute();

            LOG.info("Message id: " + message.getId());
            LOG.info(message.toPrettyString());
            LOG.info("Email Sent: " + subject + " to " + emailAddress + "\n");
        } catch (IOException e) {
            LOG.error("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            LOG.error("MessagingException: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Create a message from an email. Converts Mime to gmail format
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}