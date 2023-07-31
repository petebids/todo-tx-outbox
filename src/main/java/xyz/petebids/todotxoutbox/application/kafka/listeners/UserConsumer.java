package xyz.petebids.todotxoutbox.application.kafka.listeners;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import xyz.petebids.todotxoutbox.UserEvent;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaComponent;
import xyz.petebids.todotxoutbox.application.kafka.mapper.KafkaUserMapper;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.domain.service.UserService;


@RequiredArgsConstructor
@Slf4j
@KafkaComponent
public class UserConsumer {

    private final KafkaUserMapper userMapper;
    private final UserService userService;

    @Timed("keycloak.user-consumer")
    @KafkaListener(topics = "users",
            groupId = "user-replication-consumer",
            containerFactory = "avroContainerFactory"
    )
    public void handleUserUpsert(ConsumerRecord<String, UserEvent> consumerRecord) {

        final UserEvent user = consumerRecord.value();

        final NewExternalUser newExternalUser = userMapper.fromAvro(user);

        userService.storeExternalUser(newExternalUser);


    }


}
