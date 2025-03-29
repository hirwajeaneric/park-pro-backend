package com.park.ParkPro.service;

import com.park.ParkPro.exception.BusinessRuleException;
import com.park.ParkPro.model.User;
import com.park.ParkPro.repository.UserRepository;
import com.park.ParkPro.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+id));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessRuleException(("Email already in use"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, User updatedUser) {
        User existingUser = getUserById(id);
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new BusinessRuleException(("Email already in use"));
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setPark(updatedUser.getPark());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deactivateUser(UUID id) {
        User user = getUserById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByPark(UUID parkId) {
        return userRepository.findAllByParkId(parkId);
    }

    @Transactional(readOnly = true)
    public List<User> searchByName(String name) {
        return userRepository.searchByName(name);
    }
}
