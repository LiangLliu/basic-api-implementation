package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.entity.TradeEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
    Optional<TradeEntity> findByRsEvent(RsEventEntity rsEvent);
}
