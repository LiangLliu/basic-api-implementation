package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.repository.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
}
