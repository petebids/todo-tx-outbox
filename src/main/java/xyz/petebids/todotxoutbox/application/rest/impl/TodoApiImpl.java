package xyz.petebids.todotxoutbox.application.rest.impl;


import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import xyz.petebids.todotxoutbox.application.rest.TodosApi;
import xyz.petebids.todotxoutbox.application.rest.mapper.ResourceMapper;
import xyz.petebids.todotxoutbox.application.rest.model.NewTodoRequest;
import xyz.petebids.todotxoutbox.application.rest.model.TodoResource;
import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.domain.service.TodoService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TodoApiImpl implements TodosApi {

    private final TodoService todoService;
    private final ResourceMapper resourceMapper;

    @Timed("todo.complete")
    @Override
    public ResponseEntity<TodoResource> completeTodo(String todoId) {


        final Todo todo = todoService.markCompleted(UUID.fromString(todoId));

        final TodoResource todoResource = resourceMapper.convert(todo);

        return new ResponseEntity<>(todoResource, HttpStatus.OK);
    }

    @Timed("todo.create")
    @Override
    public ResponseEntity<TodoResource> createTodo(NewTodoRequest newTodoRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Jwt principal = (Jwt) authentication.getPrincipal();
        log.info("creating {} for {} with {}", newTodoRequest, principal.getId(), principal.getClaims());
        final NewTodoCommand command = new NewTodoCommand(newTodoRequest.getDetails(), principal.getSubject());

        final Todo todo = todoService.create(command);

        final TodoResource todoResource = resourceMapper.convert(todo);
        return new ResponseEntity<>(todoResource, HttpStatus.CREATED);
    }
}
