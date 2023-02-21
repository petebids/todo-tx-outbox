package xyz.petebids.todotxoutbox.infrastructure.event;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import xyz.petebids.todotxoutbox.infrastructure.entity.OutboxEntity;
import xyz.petebids.todotxoutbox.infrastructure.repository.OutboxRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionalOutboxEventPublisherImpl implements EventPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaAvroSerializer serializer;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void publish(byte[] payload, String aggregateType, String eventName, String aggregateId) {

        final OutboxEntity outboxEntity = new OutboxEntity();
        outboxEntity.setAggregateId(aggregateId);

        outboxEntity.setPayload(payload);
        outboxEntity.setAggregateType(aggregateType);
        outboxEntity.setType(eventName);

        /*
        jdbcTemplate.update("insert into outbox_entity (aggregateid, aggregatetype, payload, type, id) values (?, ?, ?, ?, ?)",
                aggregateId,
                aggregateType,
                bytes,
                eventName,
                UUID.randomUUID()
        );
*/
        outboxRepository.save(outboxEntity);
    }
}
