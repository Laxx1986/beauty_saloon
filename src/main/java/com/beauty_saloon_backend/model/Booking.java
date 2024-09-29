package com.beauty_saloon_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceProvider_id")
    private ServiceProvider serviceProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private SaloonService saloonService;

    @Column(name = "booking_date")
    private LocalDate date;

    @Column(name = "booking_time")
    private LocalTime time;

    @Column(name = "comment")
    private String comment;

    private UUID serviceProviderID;

    @Column(name = "confirmed", columnDefinition = "boolean default false")
    private boolean confirmed = false;
}
