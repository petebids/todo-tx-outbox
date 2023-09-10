package xyz.petebids.todotxoutbox.domain.service;

import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void storeExternalUser(NewExternalUser newExternalUser);

    Optional<User> findById(UUID id, boolean stronglyConsistentRead);
}
