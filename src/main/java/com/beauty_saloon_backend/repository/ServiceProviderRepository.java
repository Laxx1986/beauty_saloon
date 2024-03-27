package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    ServiceProvider findByServiceProviderName(String serviceProviderName);
}


