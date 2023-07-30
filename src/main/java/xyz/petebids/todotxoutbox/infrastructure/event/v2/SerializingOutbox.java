package xyz.petebids.todotxoutbox.infrastructure.event.v2;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import xyz.petebids.todotxoutbox.infrastructure.event.TransactionalOutbox;

@RequiredArgsConstructor
public class SerializingOutbox {

    private final TransactionalOutbox delegate;
    private final KafkaAvroSerializer serializer;

    public void append(Object event, String aggregateId, String aggregateType, String eventType) {
        byte[] eventBytes = serializer.serialize(aggregateType, event);
        delegate.append(eventBytes, aggregateType, eventType, aggregateId);

    }
}
