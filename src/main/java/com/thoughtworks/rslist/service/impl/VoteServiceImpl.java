package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VoteServiceImpl implements VoteService {


    private final VoteRepository voteRepository;


    public VoteServiceImpl(VoteRepository voteRepository, RsEventRepository rsEventRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public VoteDto save(VoteDto voteDto) {

        VoteEntity voteEntity = voteRepository.saveAndFlush(Vote.toVoteEntity(voteDto));
        return VoteDto.from(voteEntity);
    }

    @Override
    public Integer findByRsEventVoteCount(RsEventDto rsEventDto) {

        return voteRepository
                .findByRsEvent(rsEventDto.getId())
                .stream()
                .map(VoteEntity::getNumber)
                .reduce(0, Integer::sum);

    }
}
