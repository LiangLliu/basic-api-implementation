package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

            mockMvc.perform(get("/rs/1"))
                    .andExpect(jsonPath("$.eventName", is("第一条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/2"))
                    .andExpect(jsonPath("$.eventName", is("第二条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/3"))
                    .andExpect(jsonPath("$.eventName", is("第三条事件")))
                    .andExpect(jsonPath("$.keyWord", is("无分类")))
                    .andExpect(status().isOk());

        }

        @Test
        public void should_get_a_list_of_ranges_when_given_between_two_index() throws Exception {

            mockMvc.perform(get("/rs/list?start=1&end=2"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list?start=2&end=3"))
                    .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list?start=1&end=3"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[2].keyWord", is("无分类")))
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

            RsEvent rsEvent = RsEvent.builder().eventName("第四条事件").keyWord("无分类").build();

            ObjectMapper objectMapper = new ObjectMapper();
            String rsEventRequest = objectMapper.writeValueAsString(rsEvent);

            mockMvc.perform(post("/rs/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                    .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                    .andExpect(jsonPath("$[3].keyWord", is("无分类")))
                    .andExpect(status().isOk());
        }
    }

    /**
     * post 请求
     */
    @Nested
    public class PutRequestTest {
        @Test
        public void should_update_one_rs_event_when_given_index_and_eventName() throws Exception {

            RsEvent rsEvent = RsEvent.builder().eventName("第1条事件").build();

            ObjectMapper objectMapper = new ObjectMapper();
            String rsEventRequest = objectMapper.writeValueAsString(rsEvent);


            mockMvc.perform(put("/rs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[0].eventName", is("第1条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(status().isOk());


            rsEvent.setEventName(null);
            rsEvent.setKeyWord("类别二");
            rsEventRequest = objectMapper.writeValueAsString(rsEvent);


            mockMvc.perform(put("/rs/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("类别二")))
                    .andExpect(status().isOk());

            rsEvent.setEventName("第3条事件");
            rsEvent.setKeyWord("类别三");
            rsEventRequest = objectMapper.writeValueAsString(rsEvent);

            mockMvc.perform(put("/rs/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rsEventRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/rs/list"))
                    .andExpect(jsonPath("$[0].eventName", is("第1条事件")))
                    .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                    .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                    .andExpect(jsonPath("$[1].keyWord", is("类别二")))
                    .andExpect(jsonPath("$[2].eventName", is("第3条事件")))
                    .andExpect(jsonPath("$[2].keyWord", is("类别三")))
                    .andExpect(status().isOk());
        }


    }


}