package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.request.UserRequest;
import com.thoughtworks.rslist.service.UserService;

import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(UserRequest userRequest) {
        userRepository.save(User.userRequestToUserEntity(userRequest));
    }

    @Override
    public UserDto getUserById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        return UserDto.from(userEntity);
    }

    @Override
    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
