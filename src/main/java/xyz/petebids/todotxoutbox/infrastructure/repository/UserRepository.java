package xyz.petebids.todotxoutbox.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findById(UUID id);
}
