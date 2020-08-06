package com.thoughtworks.rslist.response;

import com.thoughtworks.rslist.dto.RsEventDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RsEventResponse {
    private Integer id;
    private String eventName;
    private Integer voteNum;
    private String keyword;


    public static RsEventResponse from(RsEventDto rsEventDto, Integer count) {
        return RsEventResponse.builder()
                .id(rsEventDto.getId())
                .eventName(rsEventDto.getEventName())
                .keyword(rsEventDto.getKeyWord())
                .voteNum(count)
                .build();
    }
}

