package com.thoughtworks.rslist.repository.entity;

import com.thoughtworks.rslist.service.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.Instant;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    @Builder.Default
    private Integer voteNum = 10;

    private Instant createTime;

    private Instant updateTime;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userId")
    private List<RsEventEntity> eventEntities;

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getGender())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();
    }
}
