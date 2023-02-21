package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.petebids.todotxoutbox.infrastructure.entity.OutboxEntity;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
}
