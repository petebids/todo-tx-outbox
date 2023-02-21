package xyz.petebids.todotxoutbox.domain.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserEntity convert(NewExternalUser newExternalUser);
}
