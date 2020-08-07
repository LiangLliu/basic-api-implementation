package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.controller.dto.RsEventRequest;
import com.thoughtworks.rslist.controller.dto.VoteRequest;
import com.thoughtworks.rslist.controller.dto.RsEventResponse;
import com.thoughtworks.rslist.service.RsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ControllerAdvice
@RestController
@RequestMapping("/rs")
@Slf4j
public class RsController {

    private final RsEventService rsEventService;

    public RsController(RsEventService rsEventService) {
        this.rsEventService = rsEventService;
    }

    @GetMapping("/{rsEventId}")
    public ResponseEntity<RsEventResponse> getOneRsEvent(@PathVariable Integer rsEventId) {
        return ResponseEntity.ok(rsEventService.getRsEventById(rsEventId));
    }

    @PostMapping("/event")
    public ResponseEntity<HttpStatus> addOneRsEvent(@RequestBody @Valid RsEventRequest rsEventRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("id", String.valueOf(rsEventService.save(rsEventRequest)))
                .build();
    }

    @PutMapping("/{rsEventId}")
    public ResponseEntity<HttpStatus> updateOneRsEvent(@PathVariable Integer rsEventId,
                                                       @RequestBody @Valid RsEventRequest rsEventRequest) {

        rsEventService.update(rsEventId, rsEventRequest);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping("/vote/{rsEventId}")
    public ResponseEntity<HttpStatus> voteRsEvent(@PathVariable Integer rsEventId,
                                                  @RequestBody @Valid VoteRequest voteRequest) {

        rsEventService.voteRsEven(rsEventId, voteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
