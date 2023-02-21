package xyz.petebids.todotxoutbox.application.rest.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.application.rest.model.TodoResource;
import xyz.petebids.todotxoutbox.domain.model.Todo;

import java.net.URI;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    TodoResource convert(Todo todo);

    default URI map(String creator) {
        return URI.create(String.format("http://localhost:4080/mydomain/users/%s", creator));
    }


}
