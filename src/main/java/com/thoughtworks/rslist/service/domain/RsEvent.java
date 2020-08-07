package com.thoughtworks.rslist.service.domain;

import com.thoughtworks.rslist.controller.dto.RsEventDto;
import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.controller.dto.RsEventRequest;
import lombok.*;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEvent {

    private String eventName;

    private String keyWord;

    private User user;

    private Instant createTime;
    private Instant updateTime;


}
