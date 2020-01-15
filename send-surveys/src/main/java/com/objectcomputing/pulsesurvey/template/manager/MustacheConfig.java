package com.objectcomputing.pulsesurvey.template.manager;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;

import java.io.File;

@Factory
public class MustacheConfig {

    @Bean
    MustacheFactory getFactory(@Value("${root}") String templateRoot) {
        return new DefaultMustacheFactory(new File(templateRoot));
    }

}
