package xyz.petebids.todotxoutbox.application.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.petebids.todotxoutbox.application.kafka.mapper.KafkaUserMapper;
import xyz.petebids.todotxoutbox.application.kafka.model.KeycloakUserChangeDetail;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.domain.service.UserService;

@RequiredArgsConstructor
@Slf4j
public class KeycloakUsersConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaUserMapper userMapper;
    private final UserService userService;

    @Timed("keycloak.user-consumer")
    @SneakyThrows
    @KafkaListener(topics = "keycloak.public.user_entity", groupId = "user-replication-consumer")
    public void handleUser(ConsumerRecord<String, String> record) {

        final KeycloakUserChangeDetail userChangeDetail = objectMapper.readValue(record.value(), KeycloakUserChangeDetail.class);

        final KeycloakUserChangeDetail.Payload payload = userChangeDetail.getPayload();

        final KeycloakUserChangeDetail.User user = payload.getAfter();
        final NewExternalUser newExternalUser = userMapper.convert(user);

        userService.storeExternalUser(newExternalUser);


    }
}
