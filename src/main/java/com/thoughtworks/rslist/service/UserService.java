package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.request.UserRequest;

public interface UserService {

    void addUser(UserRequest userRequest);

    UserDto getUserById(Integer userId);

    void deleteUserById(Integer userId);

    boolean isExistUserById(Integer userId);

    UserDto updateUser(UserDto userDto);
}
