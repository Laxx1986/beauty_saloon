package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.UserRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRightsRepository extends JpaRepository<UserRights, UUID> {

}
