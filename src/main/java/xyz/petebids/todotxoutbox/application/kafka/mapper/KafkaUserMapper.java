package xyz.petebids.todotxoutbox.application.kafka.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.application.kafka.model.KeycloakUserChangeDetail;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
@Mapper(componentModel = "spring")
public interface KafkaUserMapper {

    NewExternalUser convert(KeycloakUserChangeDetail.User u);
}
