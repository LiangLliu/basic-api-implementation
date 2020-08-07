package com.thoughtworks.rslist.controller.dto;

import com.thoughtworks.rslist.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;

    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    private Integer voteNum;

    private Instant createTime;
    private Instant updateTime;

    public static UserDto from(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .voteNum(userEntity.getVoteNum())
                .build();
    }
}
