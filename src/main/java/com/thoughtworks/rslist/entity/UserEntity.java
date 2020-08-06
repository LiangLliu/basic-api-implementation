package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private Integer voteNum;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userId")
    private List<RsEventEntity> eventEntities;

}
