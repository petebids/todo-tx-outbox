package xyz.petebids.todotxoutbox.infrastructure.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface KeycloakClient {


    @GetExchange("/mydomain/users/{id}")
    KeycloakUser get(@PathVariable String id);



}
