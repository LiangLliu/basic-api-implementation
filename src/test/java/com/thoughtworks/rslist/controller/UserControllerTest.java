package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.controller.dto.UserDto;
import com.thoughtworks.rslist.repository.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;

import com.thoughtworks.rslist.controller.dto.UserRequest;
import com.thoughtworks.rslist.service.domain.RsEvent;
import com.thoughtworks.rslist.service.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.test.web.servlet.MockMvc;


import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    public class GetMethod {
        @Test
        public void should_return_one_user_when_given_one_exist_user_id() throws Exception {

            UserDto userDto = UserDto.builder()
                    .id(1)
                    .userName("name 0")
                    .gender("male")
                    .age(19)
                    .voteNum(10)
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
    }

    @Nested
    public class PostMethod {

        @Test
        public void should_add_a_user_when_given_user_json() throws Exception {
            UserRequest userRequest = UserRequest.builder()
                    .userName("name 0")
                    .gender("male")
                    .age(19)
                    .phone("11234567890")
                    .email("a@b.com").build();
            String saveUser = objectMapper.writeValueAsString(userRequest);

            int size = userRepository.findAll().size();
            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(saveUser))
                    .andExpect(status().isCreated());

            assertEquals(size + 1, userRepository.findAll().size());
        }


        @Test
        public void should_not_add_one_user_when_given_one_user_username_is_null() throws Exception {
            postCheckAddUserValidation(
                    UserRequest.builder()
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
                    UserRequest.builder()
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
                    UserRequest.builder()
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
                    UserRequest.builder()
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
                    UserRequest.builder()
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
                    UserRequest.builder()
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
                    UserRequest.builder()
                            .userName("abcdrfg")
                            .age(20)
                            .gender("male")
                            .email("b@thoughtworks.com")
                            .phone("123456789")
                            .build()
            );
        }

        private void postCheckAddUserValidation(UserRequest userRequest) throws Exception {

            String request = objectMapper.writeValueAsString(userRequest);

            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(jsonPath("$.error", is("invalid param")))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class DeleteMethod {
        @Test
        public void should_delete_one_user_when_given_one_exist_user_id() throws Exception {

            mockMvc.perform(delete("/user/1"))
                    .andExpect(status().isOk());

            List<UserEntity> userEntities = userRepository.findAll();
            assertEquals(0, userEntities.size());
        }
    }

}