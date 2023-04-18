package xyz.petebids.todotxoutbox.domain.service;

import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.model.Todo;

import java.util.UUID;

public interface TodoService {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @SneakyThrows
    Todo create(NewTodoCommand command);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Todo markCompleted(UUID id);
}
