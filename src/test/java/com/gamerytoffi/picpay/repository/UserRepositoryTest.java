package com.gamerytoffi.picpay.repository;

import com.gamerytoffi.picpay.domain.user.User;
import com.gamerytoffi.picpay.domain.user.UserRepository;
import com.gamerytoffi.picpay.domain.user.UserType;
import com.gamerytoffi.picpay.domain.user.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private User createUser(UserDTO dto) {
        User user = new User(dto);
        entityManager.persist(user);
        return user;
    }

    @Test
    @DisplayName("should get user successfully")
    void findUserByDocumentCase1() {
        String document = "2323232323";
        UserDTO userDTO = new UserDTO("test", "Silva", document, "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.COMMON);
        this.createUser(userDTO);
        Optional<User> user = this.userRepository.findUserByDocument(document);
        assertThat(user).isPresent();
    }

    @Test
    @DisplayName("should not get user when not exist the user in database")
    void findUserByDocumentCase2() {
        String document = "2323232323";
        Optional<User> user = this.userRepository.findUserByDocument(document);
        assertThat(user).isNotPresent();
    }
}