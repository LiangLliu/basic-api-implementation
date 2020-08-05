package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RsControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
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
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                    .andExpect(status().isOk());

        }

        @Test
        public void should_get_one_rs_event_when_given_event_id() throws Exception {

            mockMvc.perform(get("/rs/0"))
                    .andExpect(jsonPath("$.eventName", is("第一条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/1"))
                    .andExpect(jsonPath("$.eventName", is("第二条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/2"))
                    .andExpect(jsonPath("$.eventName", is("第三条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_return_bad_request_when_given_a_index_out_of_round() throws Exception {
            mockMvc.perform(get("/rs/10"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("invalid index")));
        }

        @Test
        public void should_get_a_list_of_ranges_when_given_between_two_index() throws Exception {

            mockMvc.perform(get("/rs/list?start=0&end=2"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list?start=1&end=3"))
                    .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list?start=0&end=3"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                    .andExpect(status().isOk());



        }

        @Test
        public void should_return_bad_request_when_given_invalid_out_of_round() throws Exception {
            mockMvc.perform(get("/rs/list?start=0&end=10"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("invalid request para")));
        }

        @Test
        public void should_get_a_user_list_when_given_a_get_list_request() throws Exception {

            mockMvc.perform(get("/rs/users"))
                    .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                    .andExpect(jsonPath("$[0].user_age", is(20)))
                    .andExpect(jsonPath("$[0].user_gender", is("male")))
                    .andExpect(jsonPath("$[0].user_email", is("b@thoughtworks.com")))
                    .andExpect(jsonPath("$[0].user_phone", is("11234567890")))
                    .andExpect(status().isOk());
        }
    }

    /**
     * post 请求
     */
    @Nested
    public class PostRequestTest {
        @Test
        public void should_add_one_rs_event_when_given_eventName_and_keyWord() throws Exception {

            addRsEventAndUser(User.builder()
                            .userName("xiaoming")
                            .age(19)
                            .gender("female")
                            .email("a@thoughtworks.com")
                            .phone("18888888888")
                            .build(),
                    2);
        }

        @Test
        public void should_add_one_rs_event_but_not_add_user_when_given_rs_event_and_existed_user() throws Exception {

            addRsEventAndUser(User.builder()
                            .userName("xiaowang")
                            .age(20)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build(),
                    1);
        }

        private void addRsEventAndUser(User user, Integer expiredLength) throws Exception {

            RsEvent rsEvent = RsEvent.builder().eventName("第四条事件").keyWord("无分类").user(user).build();

            String rsEventRequest = objectMapper.writeValueAsString(rsEvent);

            mockMvc.perform(post("/rs/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(header().string("index", "3"))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/rs/users"))
                    .andExpect(jsonPath("$.length()").value(expiredLength))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/3"))
                    .andExpect(jsonPath("$.user.user_name", is(rsEvent.getUser().getUserName())))
                    .andExpect(status().isOk());


        }


        @Test
        public void should_not_add_one_user_when_given_one_user_username_is_null() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName(null)
                            .age(20)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );
        }

        @Test
        public void should_not_add_one_user_when_given_one_user_username_length_than_8() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfghi")
                            .age(20)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );
        }

        @Test
        public void should_not_add_one_user_when_given_one_user_gender_is_null() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfg")
                            .age(20)
                            .gender(null)
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );

        }

        @Test
        public void should_not_add_one_user_when_given_one_user_age_is_null() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfg")
                            .age(null)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );
        }

        @Test
        public void should_not_add_one_user_when_given_one_user_age_is_Less_than_18_greater_than_100() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfg")
                            .age(102)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );

        }

        @Test
        public void should_not_add_one_user_when_given_one_user_email_is_error() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfg")
                            .age(20)
                            .gender("male")
                            .email("thoughtworks.com")
                            .phone("11234567890")
                            .build()
            );
        }

        @Test
        public void should_not_add_one_user_when_given_one_user_number_is_not_11_digits() throws Exception {

            postCheckAddUserValidation(
                    User.builder()
                            .userName("abcdrfg")
                            .age(20)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("123456789")
                            .build()
            );
        }

        private void postCheckAddUserValidation(User user) throws Exception {

            RsEvent rsEvent = RsEvent.builder().eventName("第四条事件").keyWord("无分类").user(user).build();

            String rsEventRequest = objectMapper.writeValueAsString(rsEvent);

            mockMvc.perform(post("/rs/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(get("/rs/users"))
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(status().isOk());

        }

    }

    /**
     * Put 请求
     */
    @Nested
    public class PutRequestTest {
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