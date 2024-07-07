package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, UUID> {

    @Query("SELECT ot FROM OpeningTime ot WHERE ot.serviceProvider.serviceProviderId = :serviceProviderId AND ot.date = :date")
    Optional<OpeningTime> findByServiceProviderAndDate(@Param("serviceProviderId") UUID serviceProviderId, @Param("date") LocalDate date);

}
