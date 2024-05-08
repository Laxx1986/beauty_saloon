package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    ServiceProvider findByServiceProviderName(String serviceProviderName);

    @Query("SELECT sp.serviceProviderId, sp.serviceProviderName, u.userName FROM ServiceProvider sp INNER JOIN User u ON sp.User.userId = u.userId")
    List<Object[]> findServiceProviderAndUserDetails();
}


