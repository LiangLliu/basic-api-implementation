package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;

import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void add(User user) {

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        userRepository.save(userEntity);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId).get();
        return UserDto.from(userEntity);
    }
}
