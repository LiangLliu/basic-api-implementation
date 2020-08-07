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
    private final static String USER_ID_IS_INVALID = "user id is invalid";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserRequest userRequest) {
        userRepository.save(UserEntity.from(userRequest.toUser()));
    }

    public UserDto getUserById(Integer userId) {

        UserEntity userEntity = getUserEntityAndCheck(userId);
        return UserDto.from(userEntity);
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserEntity getUserEntityAndCheck(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(USER_ID_IS_INVALID);
        });
    }


}
