package com.thoughtworks.rslist.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequest {

    @NotNull
    private Integer voteNum;

    @NotNull
    private Integer userId;

}
