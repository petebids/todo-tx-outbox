package xyz.petebids.todotxoutbox.application.rest.impl;


import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import xyz.petebids.todotxoutbox.application.rest.TodosApi;
import xyz.petebids.todotxoutbox.application.rest.mapper.ResourceMapper;
import xyz.petebids.todotxoutbox.application.rest.model.NewTodoRequest;
import xyz.petebids.todotxoutbox.application.rest.model.QueryPage;
import xyz.petebids.todotxoutbox.application.rest.model.TodoResource;
import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.domain.service.TodoService;

import java.util.List;
import java.util.Optional;
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

        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final NewTodoCommand command = new NewTodoCommand(newTodoRequest.getDetails(), jwt.getSubject());

        final Todo todo = todoService.create(command);

        final TodoResource todoResource = resourceMapper.convert(todo);

        return new ResponseEntity<>(todoResource, HttpStatus.CREATED);


    }

    @Timed("todo.retrieveTodo")
    @Override
    public ResponseEntity<TodoResource> retrieveTodo(String todoId) {

        Todo todo = todoService.getById(UUID.fromString(todoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TodoResource todoResource = resourceMapper.convert(todo);

        return new ResponseEntity<>(todoResource, HttpStatus.OK);
    }


    @Timed("todo.retrieveTodos")
    @Override
    public ResponseEntity<QueryPage> retrieveTodos(Optional<String> filterOpt, Optional<String> sortOpt) {

        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String processedFilter = processFilter(filterOpt, jwt);
        final String sort = sortOpt.orElse("");

        List<Todo> userTodos = todoService.getUserTodos(processedFilter, sort);

        QueryPage page = resourceMapper.convertPage(userTodos);

        return new ResponseEntity<>(page, HttpStatus.OK);

    }

    // TODO add shared with condition
    private static String processFilter(Optional<String> filter, Jwt jwt) {
        String processedFilter = filter.map(f -> f + ";" + "userId=='%s'".formatted(jwt.getSubject()))
                .orElse("userId=='%s'".formatted(jwt.getSubject()));
        return processedFilter;
    }
}
