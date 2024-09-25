package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.dto.ServiceDetailsDTO;
import com.beauty_saloon_backend.model.SaloonService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface SaloonServiceRepository extends JpaRepository<SaloonService, UUID> {


    @Query("SELECT ss FROM SaloonService ss WHERE ss.serviceProvider.serviceProviderId = :serviceProviderId")
    List<SaloonService> findByServiceProviderId(UUID serviceProviderId);

    @Query("SELECT new com.beauty_saloon_backend.dto.ServiceDetailsDTO(s.serviceId, s.serviceName, s.servicePrice, " +
            "sl.serviceLength, sp.serviceProviderName) " +
            "FROM SaloonService s " +
            "JOIN s.serviceLength sl " +
            "JOIN s.serviceProvider sp")
    List<ServiceDetailsDTO> findAllServiceDetailsWithNames();






}
