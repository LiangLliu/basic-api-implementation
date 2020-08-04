package com.thoughtworks.rslist.api;

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

    @Test
    public void should_get_rs_list_when_given_get_request() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rs_event_when_given_event_id() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(content().string("第二条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(content().string("第三条事件"))
                .andExpect(status().isOk());
    }

}