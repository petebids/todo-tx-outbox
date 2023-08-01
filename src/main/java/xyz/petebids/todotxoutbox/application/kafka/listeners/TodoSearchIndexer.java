package xyz.petebids.todotxoutbox.application.kafka.listeners;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import xyz.petebids.todotxoutbox.TodoEvent;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaComponent;

@RequiredArgsConstructor
@KafkaComponent
@Slf4j
public class TodoSearchIndexer {

    @Timed(value = "todo-search-indexer-avro",
            description = "Measures calls to index new todos for search",
            longTask = true)
    @SneakyThrows
    @KafkaListener(topics = "outbox.event.TODO",
            groupId = "todo-search-indexer-avro",
            containerFactory = "avroContainerFactory")
    public void handleTodoEventInAvro(ConsumerRecord<String, TodoEvent> consumerRecord) {
        final TodoEvent todoEvent = consumerRecord.value();
        log.info("got {}", todoEvent);


    }
}
