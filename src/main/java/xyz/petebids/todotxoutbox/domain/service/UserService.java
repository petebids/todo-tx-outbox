package xyz.petebids.todotxoutbox.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.petebids.todotxoutbox.domain.mapper.UserMapper;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;
import xyz.petebids.todotxoutbox.infrastructure.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public void storeExternalUser(NewExternalUser newExternalUser) {

        final UserEntity userEntity = userMapper.convert(newExternalUser);
        userRepository.save(userEntity);

    }

};



