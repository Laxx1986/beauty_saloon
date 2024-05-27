package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long> {
    OpeningTime findByOpeningTimeId(Long openingTimeId);

    @Query("SELECT ot FROM OpeningTime ot WHERE ot.serviceProvider.serviceProviderId = :serviceProviderId AND ot.date = :dayOfWeek")
    Optional<OpeningTime> findByServiceProviderAndDayOfWeek(@Param("serviceProviderId") Long serviceProviderId, @Param("dayOfWeek") String dayOfWeek);
}
