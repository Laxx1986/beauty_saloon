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
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="service_id", nullable = false)
    private long serviceId;

    @Column(name="service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_price", nullable = false)
    private int servicePrice;

    @ManyToOne
    @JoinColumn(name = "service_length_id")
    private ServiceLength length;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProviderId;

}
