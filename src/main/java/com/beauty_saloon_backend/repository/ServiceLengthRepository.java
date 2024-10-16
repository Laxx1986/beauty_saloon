package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.ServiceLength;
import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ServiceLengthRepository extends JpaRepository<ServiceLength, UUID> {
    ServiceLength findByServiceLengthId(UUID ServiceLengthId);

    Optional<ServiceLength> findByServiceLength(int serviceLength);

}