package xyz.petebids.todotxoutbox.domain.service;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.petebids.todotxoutbox.TodoEvent;
import xyz.petebids.todotxoutbox.domain.TransactionHelper;
import xyz.petebids.todotxoutbox.domain.command.NewTodoCommand;
import xyz.petebids.todotxoutbox.domain.mapper.TodoMapper;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.infrastructure.entity.TodoEntity;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;
import xyz.petebids.todotxoutbox.infrastructure.event.EventPublisher;
import xyz.petebids.todotxoutbox.infrastructure.repository.TodoRepository;
import xyz.petebids.todotxoutbox.infrastructure.repository.UserRepository;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TransactionHelper transactionHelper;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final TodoMapper todoMapper;
    private final KafkaAvroSerializer serializer;


    @SneakyThrows
    public Todo create(NewTodoCommand command) {

        final Supplier<Todo> todoEntitySupplier = () -> {

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
                    .setEventType("CREATED")
                    .build();


            final byte[] bytes = serializer.serialize("outbox.event.TODO", todoEvent);

            eventPublisher.publish(bytes,
                    "TODO",
                    "NEW_TODO",
                    saved.getId().toString());


            return todoMapper.convert(todo);

        };

        return transactionHelper.executeTx(todoEntitySupplier);
    }

    public Todo markCompleted(UUID id) {

        final Supplier<Todo> markCompletedSupplier = () -> {
            final TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(RuntimeException::new);

            if (Boolean.TRUE.equals(todoEntity.getCompleted())) {
                return todoMapper.convert(todoEntity);
            }

            todoEntity.setCompleted(true);

            TodoEvent todoEvent = TodoEvent.newBuilder()
                    .setComplete(todoEntity.getCompleted())
                    .setDetails(todoEntity.getDetails())
                    .setId(todoEntity.getId().toString())
                    .setEventType("COMPLETED")
                    .build();


            final TodoEntity saved = todoRepository.save(todoEntity);

            final byte[] bytes = serializer.serialize("outbox.event.TODO", todoEvent);

            eventPublisher.publish(bytes,
                    "TODO",
                    "TODO_COMPLETED",
                    todoEntity.getId().toString());

            return todoMapper.convert(saved);
        };

        return transactionHelper.executeTx(markCompletedSupplier);

    }


}
