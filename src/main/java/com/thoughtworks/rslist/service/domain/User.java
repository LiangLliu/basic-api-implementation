package com.thoughtworks.rslist.service.domain;


import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.controller.dto.UserDto;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.controller.dto.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Integer id;

    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    private Integer voteNum;

    private Instant createTime;

    private Instant updateTime;

}
