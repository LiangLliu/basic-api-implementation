package com.thoughtworks.rslist.repository;


import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {

    List<VoteEntity> findVoteEntitiesByRsEvent(RsEventEntity rsEvent);

    List<VoteEntity> findAllByCreateTimeBetween(Instant startTime, Instant endTime);


}
