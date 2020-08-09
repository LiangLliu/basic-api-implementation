package com.thoughtworks.rslist.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponse {
    private Integer id;

    private Integer number;

    private Integer userId;

    private Integer rs_event_id;

    private Instant createTime;
    private Instant updateTime;

}
