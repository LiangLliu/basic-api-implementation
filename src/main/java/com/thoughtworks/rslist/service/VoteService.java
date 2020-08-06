package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.VoteDto;

public interface VoteService {
    VoteDto save(VoteDto voteDto);


    Integer findByRsEventVoteCount(RsEventDto rsEventDto);
}
