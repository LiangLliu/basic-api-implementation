package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.service.domain.User;
import com.thoughtworks.rslist.controller.dto.UserDto;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.controller.dto.UserRequest;

import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserRequest userRequest) {
        userRepository.save(UserEntity.from(userRequest.toUser()));
    }

    public UserDto getUserById(Integer userId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("user id is invalid");
        });

        return UserDto.from(userEntity);
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }

}
