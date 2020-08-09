package com.thoughtworks.rslist.service;


import com.thoughtworks.rslist.controller.dto.VoteResponse;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.repository.entity.VoteEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public List<VoteResponse> queryIntervalTimeVotes(long start, long end) {

        Instant startTime = Instant.ofEpochSecond(start);
        Instant endTime = Instant.ofEpochSecond(end);

        return voteRepository.findAllByCreateTimeBetween(startTime, endTime)
                .stream()
                .map(this::toVoteResponse)
                .collect(Collectors.toList());
    }

    private VoteResponse toVoteResponse(VoteEntity voteEntity) {
        return VoteResponse.builder()
                .id(voteEntity.getId())
                .number(voteEntity.getNumber())
                .rs_event_id(voteEntity.getRsEvent().getId())
                .userId(voteEntity.getUser().getId())
                .updateTime(voteEntity.getUpdateTime())
                .createTime(voteEntity.getCreateTime())
                .build();
    }
}
