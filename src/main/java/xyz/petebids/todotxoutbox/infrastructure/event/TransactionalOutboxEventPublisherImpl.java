package xyz.petebids.todotxoutbox.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.petebids.todotxoutbox.infrastructure.entity.OutboxEntity;
import xyz.petebids.todotxoutbox.infrastructure.repository.OutboxRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionalOutboxEventPublisherImpl implements EventPublisher {

    private final OutboxRepository outboxRepository;

    @Override
    public void publish(byte[] payload, String aggregateType, String eventName, String aggregateId) {

        final OutboxEntity outboxEntity = new OutboxEntity();
        outboxEntity.setAggregateId(aggregateId);

        outboxEntity.setPayload(payload);
        outboxEntity.setAggregateType(aggregateType);
        outboxEntity.setType(eventName);

        outboxRepository.save(outboxEntity);

    }
}
