package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.petebids.todotxoutbox.infrastructure.entity.ProcessedMessageEntity;

public interface ProcessedMessageRepository  extends JpaRepository<ProcessedMessageEntity, String> {
}
