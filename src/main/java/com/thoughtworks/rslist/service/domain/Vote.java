package com.thoughtworks.rslist.service.domain;

import com.thoughtworks.rslist.controller.dto.VoteDto;
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
public class Vote {
    private Integer number;
    private Integer id;
    private User user;
    private RsEvent rsEvent;

    private Instant createTime;
    private Instant updateTime;

}
