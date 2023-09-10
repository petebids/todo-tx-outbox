package xyz.petebids.todotxoutbox.domain.service;

import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.model.Page;
import xyz.petebids.todotxoutbox.domain.model.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoService {
    Todo create(NewTodoCommand command);

    Todo markCompleted(UUID id);

    Optional<Todo> getById(UUID id);

    Page<Todo> getUserTodos(String filter);
}
