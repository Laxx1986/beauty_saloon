package com.beauty_saloon_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "saloon_service")
public class SaloonService {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID serviceId;

    @Column(name="service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_price", nullable = false)
    private int servicePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_length_id")
    @JsonIgnore
    private ServiceLength serviceLength;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id")
    @JsonIgnore
    private ServiceProvider serviceProvider;

}
