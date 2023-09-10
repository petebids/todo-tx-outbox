package xyz.petebids.todotxoutbox.application.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.petebids.todotxoutbox.application.rest.mapper.ResourceMapperImpl;
import xyz.petebids.todotxoutbox.application.rest.model.NewTodoRequest;
import xyz.petebids.todotxoutbox.application.rest.model.TodoResource;
import xyz.petebids.todotxoutbox.domain.model.Todo;
import xyz.petebids.todotxoutbox.domain.service.TodoService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestApiTest
class TodoApiImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @MockBean
    TodoService todoService;

    @MockBean
    ResourceMapperImpl mapper;

    @BeforeAll
    void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @NotNull
    private static Todo buildValidTodo(NewTodoRequest newTodoRequest) {
        return new Todo(UUID.randomUUID(),
                newTodoRequest.getDetails(),
                false,
                "");
    }

    @NotNull
    private static NewTodoRequest buildValidRequest() {
        final NewTodoRequest newTodoRequest = new NewTodoRequest();
        newTodoRequest.details("get bread");
        return newTodoRequest;
    }

    @NotNull
    private static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor buildValidJwt() {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("admin"),
                        new SimpleGrantedAuthority("ROLE_AUTHORIZED_PERSONNEL")))
                .jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, "username"));
    }


    @Test
    @SneakyThrows
    void givenNoAuth_whenCreateAttempted_then403() {

        final NewTodoRequest newTodoRequest = buildValidRequest();

        mockMvc.perform(
                        post("/todos")
                                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                                .content(objectMapper.writeValueAsString(newTodoRequest))
                )
                .andExpect(status().isForbidden());

        verify(todoService, never()).create(any());

    }

    @Test
    @SneakyThrows
    void givenValidAuth_whenCreateAttempted_then201() {

        final NewTodoRequest newTodoRequest = buildValidRequest();

        final Todo t = buildValidTodo(newTodoRequest);

        when(todoService.create(any())).thenReturn(t);

        when(mapper.convert(any())).thenCallRealMethod();

        final MvcResult result = mockMvc.perform(
                        post("/todos")
                                .with(buildValidJwt())
                                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                                .content(objectMapper.writeValueAsString(newTodoRequest))
                )
                .andExpect(status().isCreated())
                .andReturn();


        verify(todoService, times(1)).create(any());

        String contentAsString = result.getResponse().getContentAsString();

        final TodoResource todoResource = objectMapper.readValue(contentAsString, TodoResource.class);

        assertNotNull(todoResource.getId());
        assertEquals(newTodoRequest.getDetails(), todoResource.getDetails());


    }


}