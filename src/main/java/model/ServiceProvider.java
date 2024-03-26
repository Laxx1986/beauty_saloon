package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Column(name = "service_provider_id", nullable = false)
    private long serviceProviderId;

    @Column(name="service_provider_name", nullable = false)
    @Size(max = 50)
    private String serviceProviderName;



}
