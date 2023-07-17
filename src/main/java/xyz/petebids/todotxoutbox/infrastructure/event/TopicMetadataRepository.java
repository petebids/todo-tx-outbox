package xyz.petebids.todotxoutbox.infrastructure.event;

import java.util.Optional;

public interface TopicMetadataRepository {

    Optional<Integer> partitionsByTopic(String topicName);
}
