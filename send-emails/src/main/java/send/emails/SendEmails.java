package send.emails;

import io.micronaut.core.annotation.*;

@Introspected
public class SendEmails {

    private String name;
    private int howMany;

    public SendEmails() {
    }

    public SendEmails(String name, int howMany) {
        this.name = name;
        this.howMany = howMany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHowMany() {
        return this.howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }

    // public SendEmails name(String name) {
    //     this.name = name;
    //     return this;
    // }

    // public SendEmails howMany(int howMany) {
    //     this.howMany = howMany;
    //     return this;
    // }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", howMany='" + getHowMany() + "'" +
            "}";
    }
}

