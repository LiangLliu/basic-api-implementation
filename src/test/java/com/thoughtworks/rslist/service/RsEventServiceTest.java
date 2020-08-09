package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.controller.dto.TradeRequest;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.TradeEntity;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RsEventServiceTest {


    @Mock
    private RsEventService rsEventService;

    @Mock
    private RsEventRepository rsEventRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TradeRepository tradeRepository;


    @BeforeEach
    public void init() {
        initMocks(this);

        initMocks(this);
        rsEventService = new RsEventService(rsEventRepository,
                voteRepository,
                userRepository,
                tradeRepository);
    }


    @Test
    public void shoule_buy_rs_event_success_when_() {
        Instant now = Instant.now();

        // given
        TradeRequest tradeRequest = TradeRequest.builder().rsEventId(1)
                .amount(new BigDecimal("100.00"))
                .ranking(1)
                .userId(1)
                .build();

        UserEntity userEntity = UserEntity.builder().id(1)
                .userName("user")
                .age(20)
                .email("a@b.com")
                .phone("11234567890")
                .voteNum(5)
                .gender("male")
                .createTime(now)
                .updateTime(now)
                .build();

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .id(1)
                .eventName("event")
                .keyWord("keyword")
                .userId(1)
                .createTime(now)
                .updateTime(now)
                .userEntity(userEntity)
                .build();

        TradeEntity saveTrade = TradeEntity.builder()
                .amount(tradeRequest.getAmount())
                .rank(tradeRequest.getRanking())
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .createTime(now)
                .updateTime(now)
                .build();

        // when
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventEntity));
        when(tradeRepository.findByRsEvent(any())).thenReturn(Optional.empty());

        // then

//        verify(userRepository).findById(tradeRequest.getUserId());
//        verify(rsEventRepository).findById(tradeRequest.getRsEventId());


        TradeEntity save = verify(saveTrade);
        assertEquals("100.00", save.getAmount().toString());
    }

}