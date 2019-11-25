package get.users;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class GetUsersFunctionTest {

    @Inject
    GetUsersClient client;

    @Test
    public void testFunction() throws Exception {
    	GetUsers body = new GetUsers();
    	body.setName("get-users");
        assertEquals("get-users", client.apply(body).blockingGet().getName());
    }
}
