package xyz.petebids.todotxoutbox.domain.service.impl;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import xyz.petebids.todotxoutbox.TodoEvent;
import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.mapper.TodoMapper;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.domain.service.TodoService;
import xyz.petebids.todotxoutbox.infrastructure.entity.TodoEntity;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;
import xyz.petebids.todotxoutbox.infrastructure.event.TransactionalOutbox;
import xyz.petebids.todotxoutbox.infrastructure.repository.TodoRepository;
import xyz.petebids.todotxoutbox.infrastructure.repository.UserRepository;

import java.util.*;

import static xyz.petebids.todotxoutbox.domain.Constants.TODO_AGGREGATE_TYPE;
import static xyz.petebids.todotxoutbox.domain.Constants.TODO_TOPIC;
import static xyz.petebids.todotxoutbox.domain.Constants.TodoEventType.TODO_COMPLETED;
import static xyz.petebids.todotxoutbox.domain.Constants.TodoEventType.TODO_CREATED;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {


    private final Map<String, String> propertyPathMapper = Collections.singletonMap("userId", "createdBy.id");
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TransactionalOutbox outbox;
    private final TodoMapper todoMapper;
    private final KafkaAvroSerializer serializer;


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @SneakyThrows
    public Todo create(NewTodoCommand command) {

        UUID userId = UUID.fromString(command.creator());

        final UserEntity creator = userRepository.findById(userId)
                .orElseGet(() -> {
                    // if the user projection is not up-to-date, create an empty user using the subject claim from the JWT
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(userId);
                    return userEntity;
                });


        final TodoEntity todo = new TodoEntity();

        todo.setCreatedBy(creator);
        todo.setDetails(command.details());
        todo.setCompleted(false);

        final TodoEntity saved = todoRepository.save(todo);

        TodoEvent todoEvent = TodoEvent.newBuilder()
                .setComplete(saved.getCompleted())
                .setDetails(saved.getDetails())
                .setId(saved.getId().toString())
                .setEventType(TODO_CREATED.name())
                .build();

        final byte[] bytes = serializer.serialize(TODO_TOPIC, todoEvent);

        outbox.append(bytes,
                TODO_AGGREGATE_TYPE,
                TODO_CREATED.name(),
                saved.getId().toString());


        return todoMapper.convert(todo);


    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Todo markCompleted(UUID id) {

        final TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(RuntimeException::new);

        if (Boolean.TRUE.equals(todoEntity.getCompleted())) {
            // already marked as complete. no need to update the db nor emit an event, but for the sake of idempotence, we return the entity
            return todoMapper.convert(todoEntity);
        }

        todoEntity.setCompleted(true);

        TodoEvent todoEvent = TodoEvent.newBuilder()
                .setComplete(todoEntity.getCompleted())
                .setDetails(todoEntity.getDetails())
                .setId(todoEntity.getId().toString())
                .setEventType(TODO_COMPLETED.name())
                .build();


        final TodoEntity saved = todoRepository.save(todoEntity);

        final byte[] bytes = serializer.serialize(TODO_TOPIC, todoEvent);

        outbox.append(bytes,
                TODO_AGGREGATE_TYPE,
                TODO_COMPLETED.name(),
                todoEntity.getId().toString());

        return todoMapper.convert(saved);
    }

    @Override
    public Optional<Todo> getById(UUID id) {
        return todoRepository.findById(id)
                .map(todoMapper::convert);

    }


    @Override
    public List<Todo> getUserTodos(String filter) {

        Specification<TodoEntity> specification = RSQLJPASupport.toSpecification(filter, propertyPathMapper);

        return todoRepository.findAll(specification)
                .stream()
                .map(todoMapper::convert)
                .toList();

    }


}
