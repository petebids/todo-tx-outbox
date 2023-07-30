package xyz.petebids.todotxoutbox.infrastructure.event;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.internals.BuiltInPartitioner;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PartitionAwareTransactionalOutboxImpl implements TransactionalOutbox {

    private static final String INSERT_STATEMENT = """
            insert into outbox ( aggregate_id,aggregate_type,payload,target_partition)
            values ( ? , ? ,? ,?)
            """;

    private final JdbcTemplate jdbcTemplate;
    private final KafkaAdmin kafkaAdmin;
    private final Cache<String, Integer> topicPartitionCache = Caffeine.newBuilder()
            .build(this::getPartitionCountFromKafka);

    private int getPartitionCountFromKafka(@NotNull String topicName) {
        return kafkaAdmin.describeTopics(topicName).get(topicName).partitions().size();
    }


    @SneakyThrows
    @Override
    public void append(byte[] payload, String aggregateType, String eventName, String aggregateId) {

        Integer partitionCount = topicPartitionCache.get(aggregateType, this::getPartitionCountFromKafka);

        final int partition = BuiltInPartitioner.partitionForKey(aggregateId.getBytes(StandardCharsets.UTF_8), partitionCount);

        final int update = jdbcTemplate.update(INSERT_STATEMENT, aggregateId, aggregateType, payload, partition);

        if (update != 1) {
            throw new SQLException();
        }

    }


}
