package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.controller.dto.*;
import com.thoughtworks.rslist.exception.InvalidRankingException;
import com.thoughtworks.rslist.exception.UserNotEnoughVoteException;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.TradeEntity;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.repository.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;

import com.thoughtworks.rslist.repository.RsEventRepository;

import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class RsEventService {

    private final static String USER_ID_IS_INVALID = "user id is invalid";
    private final static String RS_EVENT_ID_IS_INVALID = "rsEvent id is invalid";
    private final static String RANKING_IS_INVALID = "ranking is invalid";
    private final static String NOT_ENOUGH_USER_VOTES = "not enough user votes";

    private final RsEventRepository rsEventRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final TradeRepository tradeRepository;

    public RsEventService(RsEventRepository rsEventRepository,
                          VoteRepository voteRepository,
                          UserRepository userRepository,
                          TradeRepository tradeRepository) {
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.tradeRepository = tradeRepository;
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


    public void voteRsEvent(Integer rsEventId, VoteRequest voteRequest) {

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

    public List<RsEventResponse> getAllRsEvent() {
        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();

        return rsEventEntities.stream()
                .map(this::toRsEventResponse)
                .collect(Collectors.toList());

    }


    public RsEventResponse toRsEventResponse(RsEventEntity rsEventEntity) {

        return RsEventResponse.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .userId(rsEventEntity.getUserId())
                .createTime(rsEventEntity.getCreateTime())
                .updateTime(rsEventEntity.getUpdateTime())
                .build();
    }

    public void tradeRsEvent(TradeRequest tradeRequest) {

        UserEntity userEntity = getUserEntityAndCheck(tradeRequest.getUserId());

        List<VoteEntity> voteEntities = voteRepository.findAll();


        Map<RsEventEntity, Integer> collect = voteEntities.stream()
                .collect(Collectors.groupingBy(VoteEntity::getRsEvent,
                        Collectors.summingInt(VoteEntity::getNumber)));

//        collect.entrySet().stream().sorted()
//                .sorted(Map.Entry.<K, V>comparingByValue()
//                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        List<RsEventEntity> eventEntities = new ArrayList<>(sortByValue(collect).keySet());


        Integer ranking = tradeRequest.getRanking();
        if (ranking > eventEntities.size()) {
            throw new InvalidRankingException(RANKING_IS_INVALID);
        }

        Instant now = Instant.now();

        tradeRepository.save(TradeEntity.builder()
                .amount(tradeRequest.getAmount())
                .rank(ranking)
                .user(userEntity)
                .rsEvent(eventEntities.get(ranking - 1))
                .createTime(now)
                .updateTime(now)
                .build());
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByValue()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}
