package com.objectcomputing.pulsesurvey.email.manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.drive.Drive;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;

@Singleton
public class GoogleDirectoryAccessor {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final NetHttpTransport httpTransport;
    private final String applicationName;

    /**
     * Creates a google directory utility for quick access
     *
     * @param applicationName the name of this application
     */
    public GoogleDirectoryAccessor(@Property(name = "oci-google.application.name")
                                       String applicationName)
            throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.applicationName = applicationName;
    }

    @Inject
    private GoogleAuthenticator authenticator;

    /**
     * Create and return the google directory access object
     *
     * @return a google directory access object
     * @throws IOException
     */
    public Directory accessGoogleDirectory() throws IOException {

        return new Directory
                .Builder(httpTransport, JSON_FACTORY, authenticator.setupCredentials())
                .setApplicationName(applicationName)
                .build();

    }

}
