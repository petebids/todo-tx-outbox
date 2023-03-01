package xyz.petebids.todotxoutbox.application.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class KeycloakUserChangeDetail {

    private Payload<User> payload;

    @Data
    public static class Payload<User> implements DebeziumChangePayload{
        private User before;
        private User after;
    }

    @Data
    public static class User{
        private String id;
        private String email;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("first_name")
        private String firstName;

    }
}
