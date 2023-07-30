package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import xyz.petebids.todotxoutbox.infrastructure.entity.TodoEntity;

import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<TodoEntity, UUID> , JpaSpecificationExecutor<TodoEntity> {

    @Query("select t from TodoEntity t where t.createdBy = :userId ")
    List<TodoEntity> getUsersTodos(String userId);
}
