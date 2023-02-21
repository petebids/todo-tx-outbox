package xyz.petebids.todotxoutbox.infrastructure.event;

public interface EventPublisher {

    void publish(byte[] payload, String aggregateType, String eventName, String aggregateId);
}
