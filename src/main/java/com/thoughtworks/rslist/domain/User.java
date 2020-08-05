package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @NotBlank
    @Size(max = 8)
    private String userName;

    private Integer age;

    @NotBlank
    private String gender;

    private String email;

    private String phone;
}
