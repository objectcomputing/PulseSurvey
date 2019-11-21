package send.emails;

import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;
import javax.inject.*;
import java.io.IOException;
import java.util.function.Supplier;

@FunctionBean("send-emails")
public class SendEmailsFunction extends FunctionInitializer implements Supplier<SendEmails> {

    @Override
    public SendEmails get() {
        SendEmails myResponse = new SendEmails();
        myResponse.setName("I work...or something...");
        myResponse.setHowMany(10);
        return myResponse;
    }

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String...args) throws IOException {
        SendEmailsFunction function = new SendEmailsFunction();
        function.run(args, (context)-> function.get());
    }    
}

