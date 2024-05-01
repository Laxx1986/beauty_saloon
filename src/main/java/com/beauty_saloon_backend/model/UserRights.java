package com.beauty_saloon_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class UserRights {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_rights_id", nullable = false)
    private long userRightsId;

    @Column(name="user_rights_name", nullable = false)
    private String userRightsName;



}
