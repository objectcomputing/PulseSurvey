package send.emails;

import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface SendEmailsClient {

    @Named("send-emails")
    Single<SendEmails> apply(@Body SendEmails body);

}
