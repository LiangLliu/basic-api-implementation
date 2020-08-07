package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.controller.dto.*;
import com.thoughtworks.rslist.exception.UserNotEnoughVoteException;
import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.repository.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;

import com.thoughtworks.rslist.repository.RsEventRepository;

import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;


@Service
public class RsEventService {

    private final static String USER_ID_IS_INVALID = "user id is invalid";
    private final static String RS_EVENT_ID_IS_INVALID = "rsEvent id is invalid";
    private final static String NOT_ENOUGH_USER_VOTES = "not enough user votes";

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
            throw new InvalidIndexException(USER_ID_IS_INVALID);
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

    public RsEventResponse getRsEventById(Integer rsEventId) {

        RsEventEntity rsEventEntity = getRsEvenAndCheck(rsEventId);
        Integer totalVote = voteRepository.findVoteEntitiesByRsEvent(rsEventEntity)
                .stream()
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

        RsEventEntity rsEventEntity = getRsEvenAndCheck(rsEventId);

        if (!userRepository.existsById(request.getUserId())) {
            throw new InvalidIndexException(USER_ID_IS_INVALID);
        }

        if (!rsEventEntity.getId().equals(request.getId())) {
            throw new InvalidIndexException(RS_EVENT_ID_IS_INVALID);
        }

        rsEventEntity.setEventName(nonEmptyAttribute(request.getEventName(), rsEventEntity.getEventName()));
        rsEventEntity.setKeyWord(nonEmptyAttribute(request.getKeyWord(), rsEventEntity.getKeyWord()));
        rsEventRepository.save(rsEventEntity);
    }


    public void voteRsEven(Integer rsEventId, VoteRequest voteRequest) {

        UserEntity userEntity = getUserEntityAndCheck(voteRequest.getUserId());

        RsEventEntity rsEventEntity = getRsEvenAndCheck(rsEventId);

        if (userEntity.getVoteNum() < voteRequest.getVoteNum()) {
            throw new UserNotEnoughVoteException(NOT_ENOUGH_USER_VOTES);
        }

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

    private RsEventEntity getRsEvenAndCheck(Integer rsEventId) {
        return rsEventRepository.findById(rsEventId).orElseThrow(() -> {
            throw new InvalidIndexException(RS_EVENT_ID_IS_INVALID);
        });
    }

    private UserEntity getUserEntityAndCheck(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIndexException(USER_ID_IS_INVALID);
        });
    }

}
