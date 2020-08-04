package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            getMethodTest("/rs/list", "[第一条事件, 第二条事件, 第三条事件]");
        }

        @Test
        public void should_get_one_rs_event_when_given_event_id() throws Exception {
            getMethodTest("/rs/1", "第一条事件");
            getMethodTest("/rs/2", "第二条事件");
            getMethodTest("/rs/3", "第三条事件");
        }

        @Test
        public void should_get_a_list_of_ranges_when_given_between_two_index() throws Exception {
            getMethodTest("/rs/list?start=1&end=2", "[第一条事件, 第二条事件]");
            getMethodTest("/rs/list?start=2&end=3", "[第二条事件, 第三条事件]");
            getMethodTest("/rs/list?start=1&end=3", "[第一条事件, 第二条事件, 第三条事件]");
        }

        private void getMethodTest(String url, String result) throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(content().string(result))
                    .andExpect(status().isOk());
        }
    }


}