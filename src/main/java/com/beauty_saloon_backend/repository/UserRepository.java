package com.beauty_saloon_backend.repository;

import jakarta.transaction.Transactional;
import com.beauty_saloon_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
