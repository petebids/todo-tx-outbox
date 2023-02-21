package xyz.petebids.todotxoutbox.application.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
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

    private final KafkaAvroDeserializer deserializer;

    @SneakyThrows
    // @KafkaListener(topics = "outbox.event.TODO", groupId = "todo-search-indexer")
    public void handleTodoEvent(ConsumerRecord<String, GenericData.Record> consumerRecord) {
        log.info("got {}", consumerRecord);

        final GenericData.Record record = consumerRecord.value();
        log.info("record {}", record);


    }

    @SneakyThrows
    @KafkaListener(topics = "outbox.event.TODO", groupId = "todo-search-indexer-avro")
    public void handleTodoEventInAvro(ConsumerRecord<String, TodoEvent> consumerRecord) {
        //fixme https://stackoverflow.com/questions/33945383/how-to-convert-from-genericrecord-to-specificrecord-in-avro-for-compatible-schem

        final TodoEvent todoEvent = consumerRecord.value();
        log.info("got {}", todoEvent);


    }
}
