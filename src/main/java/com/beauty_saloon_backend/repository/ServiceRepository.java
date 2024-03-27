package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByServiceName(String serviceName);
}
