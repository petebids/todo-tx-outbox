package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.petebids.todotxoutbox.infrastructure.entity.BufferableEventEntity;

import java.util.UUID;

public interface BufferedEventRepository extends JpaRepository<BufferableEventEntity, UUID> {
}
