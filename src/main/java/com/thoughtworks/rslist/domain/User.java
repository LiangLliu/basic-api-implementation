package com.thoughtworks.rslist.domain;


import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(RsEvent.PrivateView.class)
public class User {

    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    public static UserEntity userRequestToUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .userName(userRequest.getUserName())
                .gender(userRequest.getGender())
                .age(userRequest.getAge())
                .phone(userRequest.getPhone())
                .email(userRequest.getEmail())
                .build();
    }

    public static UserEntity userRequestToUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .userName(userDto.getUserName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .voteNum(userDto.getVoteNum())
                .build();
    }
}
