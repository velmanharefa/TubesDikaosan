package com.TubesDiKaosan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.TubesDiKaosan.ecommerce.models.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    Users findByEmail(String email);

}
