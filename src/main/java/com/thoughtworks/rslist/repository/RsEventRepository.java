package com.thoughtworks.rslist.repository;


import com.thoughtworks.rslist.repository.entity.RsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RsEventRepository extends JpaRepository<RsEventEntity, Integer> {
}
