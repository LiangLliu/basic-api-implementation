package com.thoughtworks.rslist.controller.dto;

import com.thoughtworks.rslist.repository.entity.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {
    private Integer id;

    private Integer number;

    private UserDto user;

    private RsEventDto rsEvent;

    private Instant createTime;
    private Instant updateTime;

}
