package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.request.RsEventRequest;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    public RsController(UserService userService, RsEventService rsEventService) {
        this.userService = userService;
        this.rsEventService = rsEventService;
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

    @GetMapping("/{index}")
    @JsonView(RsEvent.PrivateView.class)
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable Integer index) throws InvalidIndexException {
        if (isOutOfRound(index)) {
            throw new InvalidIndexException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index));
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
    public void updateOneRsEvent(@PathVariable Integer index, @RequestBody RsEvent rsEventRequest) {


        if (!isEmpty(rsEventRequest.getEventName()) && !isEmpty(rsEventRequest.getKeyWord())) {
            rsList.get(index).setEventName(rsEventRequest.getEventName());
            rsList.get(index - 1).setKeyWord(rsEventRequest.getKeyWord());
        }

        if (!isEmpty(rsEventRequest.getEventName()) && isEmpty(rsEventRequest.getKeyWord())) {
            rsList.get(index).setEventName(rsEventRequest.getEventName());
        }

        if (isEmpty(rsEventRequest.getEventName()) && !isEmpty(rsEventRequest.getKeyWord())) {
            rsList.get(index).setKeyWord(rsEventRequest.getKeyWord());
        }
    }

    @DeleteMapping("/{index}")
    @JsonView(RsEvent.PrivateView.class)
    public void deleteOneRsEvent(@PathVariable Integer index) throws Exception {
        if (index > rsList.size()) {
            throw new Exception("下标越界");
        }
        rsList.remove(index - 1);
    }

    private boolean isEmpty(String string) {
        return StringUtils.isEmpty(string);
    }


}
