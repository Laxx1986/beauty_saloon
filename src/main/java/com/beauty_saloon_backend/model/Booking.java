package com.beauty_saloon_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="booking_id", nullable = false)
    private long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "connect_booking_to_service_provider",
            joinColumns = @JoinColumn(name = "serviceProviderId"),
            inverseJoinColumns = @JoinColumn(name = "serviceProviderIdInverse")
    )
    private Set<ServiceProvider> serviceProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private SaloonService saloonService;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "opening_time_id")
    private OpeningTime openingTime;

    @Column(name = "booking_date")
    private Date date;

    @Column(name = "booking_time")
    private Timestamp time;

    @Column(name = "comment")
    private String comment;

}
