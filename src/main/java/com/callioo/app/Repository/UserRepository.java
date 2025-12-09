package com.callioo.app.Repository;

import org.springframework.stereotype.Repository;

import com.callioo.app.Model.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);

}
