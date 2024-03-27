package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long> {
    OpeningTime findByOpeningTimeId(Long openingTimeId);
}
