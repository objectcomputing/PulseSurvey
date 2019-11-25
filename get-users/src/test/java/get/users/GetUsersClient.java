package get.users;

import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface GetUsersClient {

    @Named("get-users")
    Single<GetUsers> apply(@Body GetUsers body);

}
