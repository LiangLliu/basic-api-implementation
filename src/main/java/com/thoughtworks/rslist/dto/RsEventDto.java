package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEventDto {
    private Integer id;
    private String eventName;
    private String keyWord;
    private Integer userId;

    public static RsEventDto from(RsEventEntity rsEventEntity) {
        return RsEventDto.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .userId(rsEventEntity.getUserId())
                .build();
    }
}
