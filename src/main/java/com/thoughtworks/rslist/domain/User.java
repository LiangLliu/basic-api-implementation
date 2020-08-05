package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(RsEvent.PrivateView.class)
public class User {

    @NotBlank
    @Size(max = 8)
    private String userName;

    @NotNull
    @Max(100)
    @Min(18)
    private Integer age;

    @NotBlank
    private String gender;

    @Email
    private String email;

    @Pattern(regexp = "^[1][0-9]{10}$")
    private String phone;
}
