package xyz.petebids.todotxoutbox.infrastructure.event;

public interface TransactionalOutbox {

    void append(byte[] payload, String aggregateType, String eventName, String aggregateId);
}
