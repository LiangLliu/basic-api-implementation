package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.controller.dto.VoteResponse;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/vote/interval/{start}/{end}")
    public ResponseEntity<List<VoteResponse>> queryIntervalTimeVotes(@PathVariable("start") long startTime,
                                                                     @PathVariable("end") long endTime) {

        List<VoteResponse> voteResponses = voteService.queryIntervalTimeVotes(startTime, endTime);
        return ResponseEntity.status(HttpStatus.OK).body(voteResponses);

    }
}
