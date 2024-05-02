package com.beauty_saloon_backend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.Set;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_rights_id")
    private UserRights userRights;
}


