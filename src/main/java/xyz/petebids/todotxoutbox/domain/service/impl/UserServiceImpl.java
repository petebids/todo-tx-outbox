package xyz.petebids.todotxoutbox.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.petebids.todotxoutbox.domain.command.NewExternalUser;
import xyz.petebids.todotxoutbox.domain.mapper.UserMapper;
import xyz.petebids.todotxoutbox.domain.model.User;
import xyz.petebids.todotxoutbox.domain.service.UserService;
import xyz.petebids.todotxoutbox.infrastructure.entity.UserEntity;
import xyz.petebids.todotxoutbox.infrastructure.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    public void storeExternalUser(NewExternalUser newExternalUser) {

        final UserEntity userEntity = userMapper.convert(newExternalUser);
        userRepository.save(userEntity);

    }

    @Override
    public Optional<User> findById(UUID id) {

        return userRepository.findById(id).map(userMapper::toDomain);
    }

}



