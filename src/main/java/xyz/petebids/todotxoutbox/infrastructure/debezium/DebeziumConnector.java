package xyz.petebids.todotxoutbox.infrastructure.debezium;

import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.DebeziumEngine.RecordCommitter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Component
public class DebeziumConnector {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DebeziumEngine<ChangeEvent<SourceRecord, SourceRecord>> engine;
    private Future<?> future;

    @PostConstruct
    void init() {

        engine = DebeziumEngine.create(Connect.class)
                .using(generateProperties())
                .notifying(this::onEvent)
                .build();


        future = executorService.submit(engine);

    }

    @SneakyThrows
    private void onEvent(List<ChangeEvent<SourceRecord, SourceRecord>> changeEvents,
                         RecordCommitter<ChangeEvent<SourceRecord, SourceRecord>> committer) {


        committer.markBatchFinished();
    }


    private Properties generateProperties() {
        Properties props = new Properties();

        props.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.put("database.dbname", "todo");
        props.put("database.hostname", "localhost");
        props.put("database.password", "postgres");
        props.put("database.user", "postgres");
        props.put("key.converter.schema.registry.url", "http://redpanda:8081");
        props.put("key.convertor", "org.apache.kafka.connect.storage.StringConverter");
        props.put("name", "todo-outbox");
        props.put("plugin.name", "pgoutput");
        props.put("table.include.list", "public.outbox_entity");
        props.put("topic.prefix", "todo");
        props.put("transforms", "outbox");
        props.put("transforms.outbox.type", "io.debezium.transforms.outbox.EventRouter");
        props.put("value.converter", "io.debezium.converters.BinaryDataConverter");
        props.put("value.converter.delegate.converter.type", "org.apache.kafka.connect.json.JsonConverter");
        props.put("value.converter.delegate.converter.type.schemas.enable", "false");
        props.put("value.converter.schema.registry.url", "http://redpanda:8081");
        props.put("heartbeat.interval.ms", "1000");
        props.put("heartbeat.action.query", "SELECT pg_logical_emit_message(false, 'heartbeat', now()::varchar)");

        return props;
    }

    @PreDestroy
    void teardown() {
        future.cancel(true);
    }
}
