package com.thoughtworks.rslist.controller.dto;

import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEventDto {
    private Integer id;
    private String eventName;
    private String keyWord;
    private Integer userId;

    private Instant createTime;
    private Instant updateTime;

    public static RsEventDto from(RsEventEntity rsEventEntity) {
        return RsEventDto.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .userId(rsEventEntity.getUserId())
                .build();
    }
}
