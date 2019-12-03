package send.surveys;

import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface SendSurveysClient {

    @Named("send-surveys")
    Single<SendSurveys> get();

}
