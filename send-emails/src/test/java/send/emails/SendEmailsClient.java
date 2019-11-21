package send.emails;

import io.micronaut.function.client.FunctionClient;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface SendEmailsClient {

    @Named("send-emails")
    Single<SendEmails> get();

}
