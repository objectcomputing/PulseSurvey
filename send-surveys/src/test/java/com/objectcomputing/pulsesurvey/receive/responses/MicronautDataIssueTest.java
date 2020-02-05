package com.objectcomputing.pulsesurvey.receive.responses;

import com.objectcomputing.pulsesurvey.model.ResponseKey;
import com.objectcomputing.pulsesurvey.repositories.ResponseKeyRepository;
import com.objectcomputing.pulsesurvey.send.surveys.SurveysControllerTest;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@MicronautTest
class MicronautDataIssueTest {

    @Inject
    SurveyResponseController itemUnderTest;

    @Inject
    private ResponseKeyRepository responseKeyRepo;

    private static final Logger LOG = LoggerFactory.getLogger(SurveysControllerTest.class);

    @Test
    void testMarkKeyAsUsed() {

        ResponseKey key = new ResponseKey();

        //Create a key...
        ResponseKey returnedKey = responseKeyRepo.save(key);
        LOG.debug("Response Key = "+returnedKey.getResponseKey());
        //run test against real table
        itemUnderTest.markKeyAsUsed(returnedKey.getResponseKey().toString());
        LOG.debug("Response Key is "+ (returnedKey.isUsed()?"used":"not used"));
    }
}