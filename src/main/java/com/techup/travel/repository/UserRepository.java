package com.techup.travel.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techup.travel.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}