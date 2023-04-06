package xyz.petebids.todotxoutbox.application.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Data

@Data
class KeycloakUserChangeDetail {
    private val payload: Payload<User>? = null

    @Data
    class Payload<User> : DebeziumChangePayload<Any?> {
        private val before: User? = null
        private val after: User? = null
    }

    @Data
    class User {
        private val id: String? = null
        private val email: String? = null

        @JsonProperty("last_name")
        private val lastName: String? = null

        @JsonProperty("first_name")
        private val firstName: String? = null
    }
}