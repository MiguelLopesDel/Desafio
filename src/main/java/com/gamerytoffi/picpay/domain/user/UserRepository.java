package com.gamerytoffi.picpay.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocument(String document);

    Optional<User> findUserById(Long document);

}
