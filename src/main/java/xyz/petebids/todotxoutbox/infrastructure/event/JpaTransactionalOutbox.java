package xyz.petebids.todotxoutbox.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.petebids.todotxoutbox.infrastructure.entity.OutboxEntity;
import xyz.petebids.todotxoutbox.infrastructure.repository.OutboxRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class JpaTransactionalOutbox implements TransactionalOutbox {

    private final OutboxRepository outboxRepository;


    @Override
    public void append(byte[] payload, String aggregateType, String eventName, String aggregateId) {

        final OutboxEntity outboxEntity = new OutboxEntity();

        outboxEntity.setAggregateId(aggregateId);
        outboxEntity.setPayload(payload);
        outboxEntity.setAggregateType(aggregateType);
        outboxEntity.setType(eventName);

        outboxRepository.save(outboxEntity);

    }
}
