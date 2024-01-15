package com.project.daybart.demo.Authentication;

import org.springframework.stereotype.Repository;

import com.project.daybart.demo.Users.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AuthenticacionRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmailAndPassword(String email, String password);

}
