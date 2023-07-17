package xyz.petebids.todotxoutbox.domain.service.impl;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import xyz.petebids.todotxoutbox.infrastructure.event.TransactionalOutboxEventPublisher;
import xyz.petebids.todotxoutbox.infrastructure.repository.TodoRepository;
import xyz.petebids.todotxoutbox.infrastructure.repository.UserRepository;

import java.util.UUID;

import static xyz.petebids.todotxoutbox.domain.Constants.TODO_AGGREGATE_TYPE;
import static xyz.petebids.todotxoutbox.domain.Constants.TODO_TOPIC;
import static xyz.petebids.todotxoutbox.domain.Constants.TodoEventType.TODO_COMPLETED;
import static xyz.petebids.todotxoutbox.domain.Constants.TodoEventType.TODO_CREATED;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TransactionalOutboxEventPublisher eventPublisher;
    private final TodoMapper todoMapper;
    private final KafkaAvroSerializer serializer;


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @SneakyThrows
    public Todo create(NewTodoCommand command) {


        final UserEntity creator = userRepository.findById(UUID.fromString(command.creator()))
                .orElseThrow(() -> new RuntimeException("user not found"));

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

        eventPublisher.publish(bytes,
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

        eventPublisher.publish(bytes,
                TODO_AGGREGATE_TYPE,
                TODO_COMPLETED.name(),
                todoEntity.getId().toString());

        return todoMapper.convert(saved);
    }


}