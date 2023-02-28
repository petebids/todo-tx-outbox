package xyz.petebids.todotxoutbox.application.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.petebids.todotxoutbox.TodoEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class TodoSearchIndexer {

    @Timed(value = "todo-search-indexer-avro",
            description = "Measures calls to index new todos for search",
            longTask = true)
    @SneakyThrows
    @KafkaListener(topics = "outbox.event.TODO",
            groupId = "todo-search-indexer-avro")
    public void handleTodoEventInAvro(ConsumerRecord<String, TodoEvent> consumerRecord) {
        //fixme https://stackoverflow.com/questions/33945383/how-to-convert-from-genericrecord-to-specificrecord-in-avro-for-compatible-schem

        final TodoEvent todoEvent = consumerRecord.value();
        log.info("got {}", todoEvent);


    }
}
