package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.request.RsEventRequest;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEvent {

    public interface PublicView {
    }

    public interface PrivateView extends PublicView {
    }


    @JsonView(PublicView.class)
    private String eventName;

    @JsonView(PublicView.class)
    private String keyWord;

    @JsonView(PrivateView.class)
    private User user;


    public static RsEventEntity toRsEntity(RsEventRequest rsEventRequest) {

        return RsEventEntity.builder()
                .eventName(rsEventRequest.getEventName())
                .keyWord(rsEventRequest.getKeyWord())
                .userId(rsEventRequest.getUserId())
                .build();
    }


    public static RsEventEntity toRsEntity(RsEventDto rsEventDto) {
        return RsEventEntity.builder()
                .id(rsEventDto.getId())
                .eventName(rsEventDto.getEventName())
                .keyWord(rsEventDto.getKeyWord())
                .userId(rsEventDto.getUserId())
                .build();
    }


}
