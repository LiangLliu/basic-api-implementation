package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
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
public class VoteDto {
    private Integer id;

    private Integer number;

    private LocalDateTime localDateTime;

    private UserDto user;

    private RsEventDto rsEvent;

    public static VoteDto from(VoteEntity voteEntity) {

        return VoteDto.builder()
                .id(voteEntity.getId())
                .number(voteEntity.getNumber())
                .localDateTime(voteEntity.getLocalDateTime())
                .user(UserDto.from(voteEntity.getUser()))
                .rsEvent(RsEventDto.from(voteEntity.getRsEvent()))
                .build();
    }
}
