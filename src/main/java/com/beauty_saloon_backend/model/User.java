package com.beauty_saloon_backend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.util.Set;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "connect_provider_to_user",
            joinColumns = @JoinColumn(name = "serviceProviderId"),
            inverseJoinColumns = @JoinColumn(name = "serviceProviderIdInverse")
    )
    private Set<ServiceProvider> serviceProvider;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;
    @Column(name="password", nullable = false)
    private String password;
}
