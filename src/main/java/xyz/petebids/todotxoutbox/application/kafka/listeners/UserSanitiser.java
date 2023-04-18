package xyz.petebids.todotxoutbox.application.kafka.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import xyz.petebids.todotxoutbox.UserEvent;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaComponent;
import xyz.petebids.todotxoutbox.application.kafka.mapper.KafkaUserMapper;
import xyz.petebids.todotxoutbox.application.kafka.model.KeycloakUserChangeDetail;

@KafkaComponent
public class UserSanitiser {


    private final KafkaTemplate<String, UserEvent> userTemplate;
    private final KafkaUserMapper mapper;
    private final ObjectMapper objectMapper;
    private final Counter dataContractBroken;

    public UserSanitiser(KafkaTemplate<String, UserEvent> userTemplate, KafkaUserMapper mapper, ObjectMapper objectMapper, MeterRegistry meterRegistry) {
        this.userTemplate = userTemplate;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.dataContractBroken = meterRegistry
                .counter("keycloak.user-consumer.datacontract", "datacontract", "keycloak");
    }


    @SneakyThrows
    @KafkaListener(topics = "keycloak.public.user_entity",
            groupId = "user-replication-consumer",
            containerFactory = "stringStringConcurrentKafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record) {

        final KeycloakUserChangeDetail userChangeDetail;

        try {
            userChangeDetail = objectMapper.readValue(record.value(), KeycloakUserChangeDetail.class);
        } catch (JsonProcessingException e) {

            dataContractBroken.increment();
            throw new RuntimeException(e);
        }

        final KeycloakUserChangeDetail.User user = userChangeDetail.getPayload().getAfter();

        if (user == null) {
            // this is a delete action
            userChangeDetail.getPayload().getBefore().getId();

        }
        final UserEvent userEvent = mapper.toAvro(user);


        userTemplate.send("users",
                userEvent.getId().toString(),
                userEvent).get();


    }

}
