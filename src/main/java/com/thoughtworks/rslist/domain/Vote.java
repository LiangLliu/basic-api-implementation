package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    private Integer number;
    private Integer id;
    private Integer userId;
    private Integer rsEventId;
    private LocalDateTime time;

    public static VoteEntity toVoteEntity(VoteDto voteDto) {

        return VoteEntity.builder()
                .id(voteDto.getId())
                .number(voteDto.getNumber())
                .localDateTime(voteDto.getLocalDateTime())
                .user(User.userRequestToUserEntity(voteDto.getUser()))
                .rsEvent(RsEvent.toRsEntity(voteDto.getRsEvent()))
                .build();
    }
}
