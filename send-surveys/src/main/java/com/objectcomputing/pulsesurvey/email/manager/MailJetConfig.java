package com.objectcomputing.pulsesurvey.email.manager;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class MailJetConfig {

    @Bean
    MailJetSender getFactory() {
        return new MailJetSender();
    }

}
