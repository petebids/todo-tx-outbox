package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.petebids.todotxoutbox.infrastructure.entity.TodoEntity;

import java.util.UUID;

public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
}
