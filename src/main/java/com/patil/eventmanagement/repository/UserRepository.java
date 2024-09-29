package com.patil.eventmanagement.repository;

import com.patil.eventmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query(value = "SELECT * FROM USERS WHERE email=:email", nativeQuery = true)
    Optional<Users> findByEmail(String email);
}
