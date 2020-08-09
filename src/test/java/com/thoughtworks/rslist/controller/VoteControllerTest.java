package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    ZoneOffset zoneOffset = ZoneOffset.of("+8");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_interval_list_given_interval_time() throws Exception {

        long startTime = LocalDateTime.of(2020, 8, 6, 0, 0, 0).toEpochSecond(zoneOffset);
        long endTime = LocalDateTime.of(2020, 8, 7, 0, 0, 0).toEpochSecond(zoneOffset);

        String url = "/vote/interval/" + startTime + "/" + endTime;

        mockMvc.perform((get(url)))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(status().isOk());

    }

}