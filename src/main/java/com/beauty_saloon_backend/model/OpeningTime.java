package com.beauty_saloon_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OpeningTime {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID openingTimeId;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Temporal(TemporalType.TIME)
    @Column(name="time_from", nullable = false)
    private Time timeFrom;

    @Temporal(TemporalType.TIME)
    @Column(name="time_to", nullable = false)
    private Time timeTo;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;
}
