package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.test.web.servlet.MockMvc;


import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void should_return_one_user_when_given_one_exist_user_id() throws Exception {

        UserDto userDto = UserDto.builder()
                .id(1)
                .userName("name 0")
                .gender("male")
                .age(19)
                .phone("11234567890")
                .email("a@b.com").build();

        MockHttpServletResponse response = mockMvc
                .perform(get("/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), objectMapper.writeValueAsString(userDto));
    }

    @Test
    public void should_add_a_user_when_given_user_json() throws Exception {

        User user = User.builder().userName("name 0")
                .gender("male")
                .age(19)
                .phone("11234567890")
                .email("a@b.com").build();
        String saveUser = objectMapper.writeValueAsString(user);

        int size = userRepository.findAll().size();

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(saveUser))
                .andExpect(status().isCreated());

        assertEquals(size + 1, userRepository.findAll().size());

    }


    @Test
    public void should_delete_one_user_when_given_one_exist_user_id() throws Exception {

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());

        List<UserEntity> userEntities = userRepository.findAll();
        assertEquals(0, userEntities.size());
    }


}