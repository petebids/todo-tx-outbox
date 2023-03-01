package xyz.petebids.todotxoutbox.application.kafka.model;

public interface DebeziumChangePayload<T> {

    T getBefore();
    T getAfter();
}
