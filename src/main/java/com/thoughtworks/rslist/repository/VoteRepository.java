package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {


    @Query(value = "SELECT * FROM VoteEntity AS v WHERE v.rsEvent.id  = ?1")
    List<VoteEntity> findByRsEvent(Integer id);
}
