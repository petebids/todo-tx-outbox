package xyz.petebids.todotxoutbox.domain.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.domain.model.User;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;
import xyz.petebids.todotxoutbox.infrastructure.rest.KeycloakUser;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity convert(NewExternalUser newExternalUser);

    User fromKeycloak(KeycloakUser keycloakUser);
}
