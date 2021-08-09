package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserID(String userID);


    boolean existsByUserID(String userID);
}
