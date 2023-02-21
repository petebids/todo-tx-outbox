package xyz.petebids.todotxoutbox.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.infrastructure.entity.TodoEntity;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(source = "createdBy.id", target = "creator")
    Todo convert(TodoEntity entity);


}
