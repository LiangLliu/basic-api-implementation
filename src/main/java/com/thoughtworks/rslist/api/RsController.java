package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.request.RsEventRequest;
import com.thoughtworks.rslist.request.VoteRequest;
import com.thoughtworks.rslist.response.RsEventResponse;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
@RestController
@RequestMapping("/rs")
@Slf4j
public class RsController {


    private final UserService userService;

    private final RsEventService rsEventService;

    private final VoteService voteService;


    private final List<RsEvent> rsList;
    private final List<User> userList;

    {
        userList = Stream.of(
                new User("xiaowang", 20, "male", "b@thoughtworks.com", "11234567890")
        ).collect(Collectors.toList());

        rsList = Stream.of(
                RsEvent.builder().eventName("第一条事件").keyWord("无分类").build(),
                RsEvent.builder().eventName("第二条事件").keyWord("无分类").build(),
                RsEvent.builder().eventName("第三条事件").keyWord("无分类").build()
        ).collect(Collectors.toList());


    }

    public RsController(UserService userService, RsEventService rsEventService, VoteService voteService) {
        this.userService = userService;
        this.rsEventService = rsEventService;
        this.voteService = voteService;
    }


    @GetMapping("/list")
    @JsonView(RsEvent.PublicView.class)
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end)
            throws InvalidRequestParamException {
        if (isOutOfRound(end)) {
            throw new InvalidRequestParamException("invalid request para");
        }

        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        return ResponseEntity.ok(rsList.subList(start, end));
    }

    @GetMapping("/{rsEventId}")
    public ResponseEntity<RsEventResponse> getOneRsEvent(@PathVariable Integer rsEventId) throws InvalidIndexException {


        if (!rsEventService.isExistUserById(rsEventId)) {
            throw new InvalidIndexException("invalid rsEventId");
        }
        RsEventDto rsEventDto = rsEventService.findById(rsEventId);
        Integer count = voteService.findByRsEventVoteCount(rsEventDto);

        return ResponseEntity.ok(RsEventResponse.from(rsEventDto, count));
    }

    private boolean isOutOfRound(@PathVariable Integer index) {
        return index > rsList.size() - 1 || index < 0;
    }

    @PostMapping("/event")
    public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEventRequest rsEventRequest) {

        if (userService.isExistUserById(rsEventRequest.getUserId())) {
            RsEventDto rsEventDto = rsEventService.save(rsEventRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("id", String.valueOf(rsEventDto.getId()))
                    .build();
        }
        throw new UserNotFoundException("user id is invalid");
    }

    @GetMapping("/users")
    @JsonView(RsEvent.PrivateView.class)
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/{index}")
    public ResponseEntity updateOneRsEvent(@PathVariable Integer index, @RequestBody @Valid RsEventRequest rsEventRequest) {

        if (!userService.isExistUserById(rsEventRequest.getUserId())) {
            throw new UserNotFoundException("user id is invalid");
        }

        RsEventDto rsEventDto = rsEventService.findById(index);
        if (rsEventDto.getUserId().equals(rsEventRequest.getId())) {

            rsEventDto.setEventName(nonEmptyAttribute(rsEventRequest.getEventName(), rsEventDto.getEventName()));
            rsEventDto.setKeyWord(nonEmptyAttribute(rsEventRequest.getKeyWord(), rsEventDto.getKeyWord()));
            rsEventService.updateRsEvent(rsEventDto);
        } else {
            throw new RsEVentIdInvalidException("rsEvent id is invalid");
        }
        return ResponseEntity.ok().build();
    }

    private String nonEmptyAttribute(String editAttribute, String nativeAttribute) {
        if (!StringUtils.isEmpty(editAttribute)) {
            return editAttribute;
        }
        return nativeAttribute;
    }

    @DeleteMapping("/{rsEventId}")
    @JsonView(RsEvent.PrivateView.class)
    public void deleteOneRsEvent(@PathVariable Integer index) throws Exception {
        if (index > rsList.size()) {
            throw new Exception("下标越界");
        }
        rsList.remove(index - 1);
    }


    @PostMapping("/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable Integer rsEventId,
                                      @RequestBody @Valid VoteRequest voteRequest) {

        if (!rsEventService.isExistUserById(rsEventId)) {
            throw new RsEVentIdInvalidException("rsEvent id is invalid");
        }

        if (!userService.isExistUserById(voteRequest.getUserId())) {
            throw new UserNotFoundException("user id is invalid");
        }

        UserDto userDto = userService.getUserById(voteRequest.getUserId());

        if (userDto.getVoteNum() < voteRequest.getVoteNum()) {
            throw new UserNotEnoughVoteException("not enough user votes");
        }

        RsEventDto rsEventDto = rsEventService.findById(rsEventId);

        voteService.save(VoteDto.builder()
                .number(voteRequest.getVoteNum())
                .rsEvent(rsEventDto)
                .user(userDto)
                .localDateTime(LocalDateTime.now())
                .build());

        userDto.setVoteNum(userDto.getVoteNum() - voteRequest.getVoteNum());
        userService.updateUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


}
