package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thoughtworks.rslist.controller.dto.TradeRequest;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.domain.RsEvent;

import com.thoughtworks.rslist.controller.dto.RsEventRequest;
import com.thoughtworks.rslist.controller.dto.VoteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {


    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private RsEventService rsEventService;

    @Autowired
    private RsEventRepository rsEventRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new RsController(userService, rsEventService)).build();
    }

    /**
     * @Nested 使用内部类的形式将同类型的测试放在一起
     * GetMethodTest : 测试所有的get请求
     */
    @Nested
    public class GetRequestTest {
        @Test
        public void should_get_rs_list_when_given_get_request() throws Exception {

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条热搜事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("娱乐")))
                    .andExpect(jsonPath("$[0].userId", is(1)))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_get_one_rs_event_when_given_event_id() throws Exception {

            mockMvc.perform(get("/rs/1"))
                    .andExpect(jsonPath("$.eventName", is("第一条热搜事件")))
                    .andExpect(jsonPath("$.keyWord", is("娱乐")))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.voteNum", is(15)))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_return_bad_request_when_given_a_index__of_rs_event_not_exist() throws Exception {
            mockMvc.perform(get("/rs/" + Integer.MAX_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("rsEvent id is invalid")));
        }


        @Test
        public void should_return_bad_request_when_given_invalid_out_of_round() throws Exception {
            mockMvc.perform(get("/rs/list?start=0&end=10"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("invalid request para")));
        }

    }

    /**
     * post 请求
     */
    @Nested
    public class PostRequestTest {


        @Test
        public void should_add_vote_when_given_rsEvent_and_vote_number() throws Exception {

            VoteRequest voteRequest = VoteRequest.builder()
                    .userId(1)
                    .voteNum(5)
                    .build();

            String request = objectMapper.writeValueAsString(voteRequest);

            mockMvc.perform(post("/rs/vote/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated());
        }


        @Test
        public void should_add_rs_event_when_user_is_exist() throws Exception {

            RsEventRequest rsEventRequest = RsEventRequest.builder()
                    .eventName("第一条热搜事件")
                    .keyWord("娱乐")
                    .userId(1).build();

            String request = objectMapper.writeValueAsString(rsEventRequest);

            long start = rsEventService.getRsListLength();

            mockMvc.perform(post("/rs/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated());

            assertEquals(start + 1, rsEventService.getRsListLength());

        }


        /**
         * 当用户不存在时，添加用户失败
         */
        @Test
        public void should_not_add_rs_event_when_user_is_not_exist() throws Exception {
            RsEventRequest rsEventRequest = RsEventRequest.builder()
                    .eventName("第一条热搜事件")
                    .keyWord("娱乐")
                    .userId(Integer.MAX_VALUE).build();

            String request = objectMapper.writeValueAsString(rsEventRequest);

            long start = rsEventService.getRsListLength();

            mockMvc.perform(post("/rs/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(jsonPath("$.error", is("user id is invalid")))
                    .andExpect(status().isBadRequest());

            assertEquals(start, rsEventService.getRsListLength());
        }

        /**
         * 购买热搜事件
         */
        @Test
        public void should_add_trade_when_user_buy_is_legal() throws Exception {

            TradeRequest tradeRequest = TradeRequest.builder()
                    .userId(1)
                    .ranking(1)
                    .rsEventId(1)
                    .amount(new BigDecimal("100.00")).build();

            mockMvc.perform(post("/rs/trade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(tradeRequest)))
                    .andExpect(status().isCreated());
        }

        /**
         * 购买热搜事件,如果存在，且出价正确
         */
        @Test
        public void should_update_trade_when_user_buy_is_legal() throws Exception {

            TradeRequest tradeRequest = TradeRequest.builder()
                    .userId(1)
                    .ranking(1)
                    .rsEventId(1)
                    .amount(new BigDecimal("200.00")).build();

            mockMvc.perform(post("/rs/trade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(tradeRequest)))
                    .andExpect(status().isCreated());
        }


        /**
         * 购买热搜事件,如果存在，且出价不正确
         */
        @Test
        public void should_not_update_trade_when_user_buy_is_not_legal() throws Exception {

            TradeRequest tradeRequest = TradeRequest.builder()
                    .userId(1)
                    .ranking(1)
                    .rsEventId(1)
                    .amount(new BigDecimal("100.00")).build();

            mockMvc.perform(post("/rs/trade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(tradeRequest)))
                    .andExpect(status().isBadRequest());
        }

    }

    /**
     * Put 请求
     */
    @Nested
    public class PutRequestTest {

        @Test
        public void should_update_rs_event_when_rs_event_userId_equal_request_userId() throws Exception {
            RsEventRequest rsEventRequest = RsEventRequest.builder()
                    .eventName("第一条热搜事件更改")
                    .keyWord("娱乐")
                    .userId(1).build();

            String request = objectMapper.writeValueAsString(rsEventRequest);

            mockMvc.perform(put("/rs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isOk());

        }

        @Test
        public void should_not_update_rs_event_when_rs_event_userId_not_equal_request_userId() throws Exception {
            RsEventRequest rsEventRequest = RsEventRequest.builder()
                    .eventName("第一条热搜事件更改")
                    .keyWord("娱乐")
                    .userId(2).build();

            String request = objectMapper.writeValueAsString(rsEventRequest);

            mockMvc.perform(put("/rs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest());

        }


        @Test
        public void should_update_rs_event_when_rs_event_userId_equal_request_userId_but_only_eventName_or_keyword() throws Exception {
            RsEventRequest rsEventRequest = RsEventRequest.builder()
                    .eventName(null)
                    .keyWord("修改娱乐测试")
                    .userId(1).build();

            String request = objectMapper.writeValueAsString(rsEventRequest);

            mockMvc.perform(put("/rs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void should_update_one_rs_event_when_given_index_and_eventName() throws Exception {

            updateRsEvent(RsEvent.builder().eventName("第1条事件").build(),
                    0);

            updateRsEvent(RsEvent.builder().eventName("第3条事件").keyWord("类别三").build(),
                    2);
        }

        private void updateRsEvent(RsEvent rsEvent, Integer rsEventId) throws Exception {
            String rsEventRequest = objectMapper.writeValueAsString(rsEvent);
            mockMvc.perform(put("/rs/" + rsEventId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/" + rsEventId))
                    .andExpect(jsonPath("$.eventName", is(rsEvent.getEventName())))
                    .andExpect(status().isOk());

        }
    }

    /**
     * delete 请求
     */
    @Nested
    public class DeleteMethodTest {

        @Test
        public void should_delete_one_rs_event_when_given_index() throws Exception {
            mockMvc.perform(delete("/rs/1"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(status().isOk());
        }

    }

}