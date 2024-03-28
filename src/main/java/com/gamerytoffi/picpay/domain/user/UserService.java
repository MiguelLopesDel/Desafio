package com.gamerytoffi.picpay.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        this.saveUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
