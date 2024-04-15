package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.ServiceLength;
import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface ServiceLengthRepository extends JpaRepository<ServiceLength, Long> {
    ServiceLength findByServiceLengthId(Long ServiceLengthId);
}