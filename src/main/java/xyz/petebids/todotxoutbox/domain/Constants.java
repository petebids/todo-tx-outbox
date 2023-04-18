package xyz.petebids.todotxoutbox.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String TODO_TOPIC = "outbox.event.TODO";

    public static final String TODO_AGGREGATE_TYPE = "TODO";

    public enum TodoEventType {
        TODO_CREATED("CREATED"),
        TODO_COMPLETED("COMPLETED");

        @Getter
        private String value;

        TodoEventType(String value) {
            this.value = value;

        }
    }
}
