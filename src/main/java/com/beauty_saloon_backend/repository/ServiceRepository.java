package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.SaloonService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ServiceRepository extends JpaRepository<SaloonService, Long> {
    SaloonService findByServiceName(String serviceName);
}
