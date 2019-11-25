package get.users;

import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;
import javax.inject.*;
import java.io.IOException;
import java.util.function.Function;

@FunctionBean("get-users")
public class GetUsersFunction extends FunctionInitializer implements Function<GetUsers, GetUsers> {

    @Override
    public GetUsers apply(GetUsers msg) {
         return msg;
    }

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String...args) throws IOException {
        GetUsersFunction function = new GetUsersFunction();
        function.run(args, (context)-> function.apply(context.get(GetUsers.class)));
    }    
}

