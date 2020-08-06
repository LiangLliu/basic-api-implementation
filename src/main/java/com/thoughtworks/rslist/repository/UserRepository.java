package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
