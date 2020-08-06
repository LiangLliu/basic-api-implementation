package com.thoughtworks.rslist.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Integer id;

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
}
