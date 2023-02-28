package xyz.petebids.todotxoutbox.application.kafka.mapper;

import org.mapstruct.Mapper;
import xyz.petebids.todotxoutbox.UserEvent;
import xyz.petebids.todotxoutbox.application.kafka.model.KeycloakUserChangeDetail;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;

@Mapper(componentModel = "spring")
public interface KafkaUserMapper {

    NewExternalUser convert(KeycloakUserChangeDetail.User u);

    default NewExternalUser fromAvro(UserEvent u) {
        if (u == null) {
            throw new NullPointerException();
        }

        return new NewExternalUser(u.getId().toString(),
                u.getFirstName().toString(),
                u.getLastName().toString(),
                u.getEmail().toString());

    }


    default UserEvent toAvro(KeycloakUserChangeDetail.User u) {
        if (u == null) {
            throw new NullPointerException();
        }
        final UserEvent.Builder builder = UserEvent.newBuilder();

        builder.setId(u.getId());
        builder.setEmail(u.getEmail());
        builder.setFirstName(u.getFirstName());
        builder.setLastName(u.getLastName());

        return builder.build();
    }
}
