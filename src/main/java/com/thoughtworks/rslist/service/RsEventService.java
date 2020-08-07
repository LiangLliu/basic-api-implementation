package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.controller.dto.*;
import com.thoughtworks.rslist.exception.UserNotEnoughVoteException;
import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.repository.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.repository.RsEventRepository;

import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;


@Service
public class RsEventService {

    private final RsEventRepository rsEventRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    public RsEventService(RsEventRepository rsEventRepository,
                          VoteRepository voteRepository,
                          UserRepository userRepository) {
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public Integer save(RsEventRequest request) {

        if (!userRepository.existsById(request.getUserId())) {
            throw new InvalidIndexException("invalid rsEventId");
        }
        RsEventEntity rsEventEntity = rsEventRepository.save(RsEventEntity.builder()
                .eventName(request.getEventName())
                .keyWord(request.getKeyWord())
                .userId(request.getUserId())
                .createTime(Instant.now())
                .updateTime(Instant.now())
                .build());
        return rsEventEntity.getId();
    }

    public long getRsListLength() {
        return rsEventRepository.count();
    }

    public RsEventDto findById(Integer rsEventId) {
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        return RsEventDto.from(rsEventEntity);
    }



    public RsEventResponse getRsEventById(Integer rsEventId) {

        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId)
                .orElseThrow(() -> {
                            throw new InvalidIndexException("invalid rsEventId");
                        }
                );
        Integer totalVote = voteRepository.findVoteEntitiesByRsEvent(rsEventEntity).stream()
                .map(VoteEntity::getNumber)
                .reduce(0, Integer::sum);

        return RsEventResponse.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .voteNum(totalVote)
                .createTime(rsEventEntity.getCreateTime())
                .updateTime(rsEventEntity.getUpdateTime())
                .build();
    }


    public void update(Integer rsEventId, RsEventRequest request) {

        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId)
                .orElseThrow(() -> {
                            throw new InvalidIndexException("rsEvent id is invalid");
                        }
                );

        if (!userRepository.existsById(request.getUserId())) {
            throw new UserNotFoundException("user id is invalid");
        }

        if (!rsEventEntity.getId().equals(request.getId())) {
            throw new InvalidIndexException("rsEvent id is invalid");
        }

        rsEventEntity.setEventName(nonEmptyAttribute(request.getEventName(), rsEventEntity.getEventName()));
        rsEventEntity.setKeyWord(nonEmptyAttribute(request.getKeyWord(), rsEventEntity.getKeyWord()));

        rsEventRepository.save(rsEventEntity);

    }


    public void voteRsEven(Integer rsEventId, VoteRequest voteRequest) {

        if (!rsEventRepository.existsById(rsEventId)) {
            throw new InvalidIndexException("rsEvent id is invalid");
        }

        UserEntity userEntity = userRepository.findById(voteRequest.getUserId()).orElseThrow(() -> {
            throw new UserNotFoundException("user id is invalid");
        });


        if (userEntity.getVoteNum() < voteRequest.getVoteNum()) {
            throw new UserNotEnoughVoteException("not enough user votes");
        }

        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId)
                .orElseThrow(() -> {
                            throw new InvalidIndexException("rsEvent id is invalid");
                        }
                );

        Instant now = Instant.now();

        voteRepository.save(VoteEntity.builder()
                .number(voteRequest.getVoteNum())
                .rsEvent(rsEventEntity)
                .user(userEntity)
                .createTime(now)
                .updateTime(now)
                .build());

        userEntity.setVoteNum(userEntity.getVoteNum() - voteRequest.getVoteNum());
        userEntity.setUpdateTime(now);

        userRepository.save(userEntity);

    }


    private String nonEmptyAttribute(String editAttribute, String nativeAttribute) {
        if (!StringUtils.isEmpty(editAttribute)) {
            return editAttribute;
        }
        return nativeAttribute;
    }
}
