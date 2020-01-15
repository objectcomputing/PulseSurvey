package com.objectcomputing.pulsesurvey.send.surveys;

import io.micronaut.function.client.FunctionClient;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface SendSurveysClient {

    @Named("send-surveys")
    Single<SendSurveys> get();

}
