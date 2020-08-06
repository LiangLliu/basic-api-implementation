package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;

public interface UserService {

    void add(User user);

    UserDto getUserById(Integer userId);

    void deleteUserById(Integer userId);
}
