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
public class ServiceLength {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="service_length_id", nullable = false)
    private long serviceLengthId;

    @Column(name="service_length", nullable = false)
    private int serviceLength;



}
