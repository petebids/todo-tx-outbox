package xyz.petebids.todotxoutbox.application.rest.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.application.rest.model.QueryPage;
import xyz.petebids.todotxoutbox.application.rest.model.TodoResource;
import xyz.petebids.todotxoutbox.domain.model.Todo;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    TodoResource convert(Todo todo);

    default QueryPage convertPage(List<Todo> todos) {

        if (todos == null) {
            throw new NullPointerException();
        }

        QueryPage page = new QueryPage();

        List<TodoResource> todoResources = todos.stream()
                .map(this::convert)
                .toList();

        page.setItems(todoResources);

        return page;

    }

    default URI map(String creator) {
        return URI.create(String.format("http://localhost:4080/mydomain/users/%s", creator));
    }


}
