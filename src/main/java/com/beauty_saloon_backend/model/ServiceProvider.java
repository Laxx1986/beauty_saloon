package com.beauty_saloon_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_provider_id",nullable = false)
    private long serviceProviderId;

    @Column(name="service_provider_name", nullable = false)
    @Size(max = 50)
    private String serviceProviderName;

    @OneToMany(mappedBy = "serviceId", fetch = FetchType.EAGER)
    private List<SaloonService> saloonServices;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User User;

}
