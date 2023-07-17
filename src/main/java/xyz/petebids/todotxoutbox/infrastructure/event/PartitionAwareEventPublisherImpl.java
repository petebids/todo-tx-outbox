package xyz.petebids.todotxoutbox.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.internals.BuiltInPartitioner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class PartitionAwareEventPublisherImpl implements EventPublisher {

    private static final String INSERT_STATEMENT = """
            insert into outbox ( aggregate_id,aggregate_type,payload,target_partition)
            values ( ? , ? ,? ,?)
            """;

    private final TopicMetadataRepository topicMetadataRepository;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public void publish(byte[] payload, String aggregateType, String eventName, String aggregateId) {

        final Integer partitionCount = topicMetadataRepository.partitionsByTopic(aggregateType).orElseThrow(() -> new RuntimeException());

        final int partition = BuiltInPartitioner.partitionForKey(aggregateId.getBytes(StandardCharsets.UTF_8), partitionCount);

        final int update = jdbcTemplate.update(INSERT_STATEMENT, aggregateId, aggregateType, payload, partition);


    }
}
