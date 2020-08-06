package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.request.RsEventRequest;
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

    @Override
    public boolean isExistUserById(Integer userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return UserDto
                .from(userRepository
                        .save(User
                                .userRequestToUserEntity(userDto))
                );
    }

}
