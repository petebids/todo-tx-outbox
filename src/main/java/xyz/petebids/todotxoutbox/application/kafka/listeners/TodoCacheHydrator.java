package xyz.petebids.todotxoutbox.application.kafka.listeners;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.kafka.annotation.KafkaListener;
import xyz.petebids.todotxoutbox.TodoEvent;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaComponent;

import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
@KafkaComponent
@Slf4j
public class TodoCacheHydrator {


    private final JdbcLockRegistry lockRegistry;

    @Timed(value = "todo-search-indexer-avro",
            description = "Measures calls to index new todos for search",
            longTask = true)
    @SneakyThrows
    @KafkaListener(topics = "outbox.event.TODO",
            groupId = "todo-search-indexer-avro",
            containerFactory = "avroContainerFactory")
    public void handleTodoEventInAvro(ConsumerRecord<String, TodoEvent> consumerRecord) {

        final TodoEvent todoEvent = consumerRecord.value();

        Lock lock = lockRegistry.obtain(todoEvent.getId());

        lock.lock();


        log.info("got {}", todoEvent);


    }
}
