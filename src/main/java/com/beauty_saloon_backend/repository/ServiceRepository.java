package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.SaloonService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Repository
public interface ServiceRepository extends JpaRepository<SaloonService, UUID> {
    SaloonService findByServiceName(String serviceName);

    @Query("SELECT ss.serviceId, ss.serviceName, ss.servicePrice, sl.serviceLength, sp1.serviceProviderName\n" +
            "FROM SaloonService  ss\n" +
            "INNER JOIN ServiceLength sl ON ss.serviceLength.serviceLengthId = sl.serviceLengthId\n" +
            "INNER JOIN ServiceProvider sp1 ON ss.serviceProvider.serviceProviderId = sp1.serviceProviderId\n")
    List<Object[]> findServiceDetails();

}
