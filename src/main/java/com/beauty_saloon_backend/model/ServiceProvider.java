package com.beauty_saloon_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ServiceProvider {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID serviceProviderId;

    @Column(name="service_provider_name", nullable = false)
    @Size(max = 50)
    private String serviceProviderName;

    @OneToMany(mappedBy = "serviceId", fetch = FetchType.EAGER)
    private List<SaloonService> saloonServices;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User User;

}
