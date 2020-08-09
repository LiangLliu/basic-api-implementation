package com.thoughtworks.rslist.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RsEventResponse {
    private Integer id;
    private String eventName;
    private Integer voteNum;
    private String keyWord;
    private Integer userId;
    private Instant createTime;
    private Instant updateTime;


    public static RsEventResponse from(RsEventDto rsEventDto, Integer count) {
        return RsEventResponse.builder()
                .id(rsEventDto.getId())
                .eventName(rsEventDto.getEventName())
                .keyWord(rsEventDto.getKeyWord())
                .voteNum(count)
                .build();
    }
}

