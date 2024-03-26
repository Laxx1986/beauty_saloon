package model;
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
            name = "service_provider_id",
            joinColumns = @JoinColumn(name = "serviceProviderId"),
            inverseJoinColumns = @JoinColumn(name = "serviceProviderId")
    )
    private Set<ServiceProvider> serviceProvider;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;
    @Column(name="password", nullable = false)
    private String password;
}
