package com.thoughtworks.rslist.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEventRequest {

    private Integer id;

    @NotBlank
    private String eventName;

    @NotBlank
    private String keyWord;

    @NotNull
    private Integer userId;

}
