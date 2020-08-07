package com.thoughtworks.rslist.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.service.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank
    @Size(max = 8)
    @JsonProperty("user_name")
    private String userName;

    @NotNull
    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    private Integer age;

    @NotBlank
    @JsonProperty("user_gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    private String email;

    @Pattern(regexp = "^[1][0-9]{10}$")
    @JsonProperty("user_phone")
    private String phone;

    public User toUser() {
        Instant now = Instant.now();
        return User.builder()
                .userName(this.userName)
                .age(this.age)
                .gender(this.gender)
                .email(this.email)
                .phone(this.phone)
                .createTime(now)
                .updateTime(now)
                .build();

    }
}
