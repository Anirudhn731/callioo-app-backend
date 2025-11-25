package com.callioo.app.Repository;

import org.springframework.stereotype.Repository;

import com.callioo.app.Model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
