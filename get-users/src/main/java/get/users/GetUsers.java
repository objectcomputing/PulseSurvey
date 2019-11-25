package get.users;

import io.micronaut.core.annotation.*;

@Introspected
public class GetUsers {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

