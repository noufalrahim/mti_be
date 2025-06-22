package com.tinysteps.tinysteps.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tinysteps.tinysteps.controller.ChildController;
import com.tinysteps.tinysteps.model.ChildModel;
import com.tinysteps.tinysteps.model.UserModel;
import com.tinysteps.tinysteps.repository.ChildRepository;
import com.tinysteps.tinysteps.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChildRepository childRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChildController.class);

    public UserService(UserRepository userRepository, ChildRepository childRepository) {
        this.userRepository = userRepository;
        this.childRepository = childRepository;
    }

    public ResponseEntity<String> addUser(String phone) {
        Optional<UserModel> existingUser = userRepository.findByPhone(phone);
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists!");
        }

        UserModel newUser = new UserModel();
        newUser.setPhone(phone);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
    }

    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<Map<String, Object>> editUser(Long id, UserModel updatedUser) {
        Optional<UserModel> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found", "data", Collections.emptyMap()));
        }

        UserModel user = existingUser.get();
        user.setPhone(updatedUser.getPhone());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User updated successfully", "data", user));
    }

    public ResponseEntity<Map<String, Object>> deleteUser(Long id) {
        Optional<UserModel> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found", "data", Collections.emptyMap()));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully", "data", Collections.emptyMap()));
    }

    public ResponseEntity<Map<String, Object>> getDefaultChild(Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid user", "data", Collections.emptyMap()));
        }

        Optional<UserModel> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Invalid user", "data", Collections.emptyMap()));
        }

        UserModel user = existingUser.get();
        ChildModel child = user.getDefaultChild();

        if (child == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No child found for user", "data", Collections.emptyMap()));
        }

        return ResponseEntity.ok(Map.of("message", "Successfully fetched default child", "data", child));
    }

    public ResponseEntity<Map<String, Object>> editDefaultChild(Long userId, Long childId) {
        if (userId == null || childId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid user or child ID", "data", Collections.emptyMap()));
        }

        Optional<UserModel> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Invalid user", "data", Collections.emptyMap()));
        }

        Optional<ChildModel> existingChild = childRepository.findById(childId);
        if (existingChild.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Child not found", "data", Collections.emptyMap()));
        }

        UserModel user = existingUser.get();
        ChildModel child = existingChild.get();

        if (child.getUser() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Child does not belong to the user", "data", Collections.emptyMap()));
        }

        user.setDefaultChild(child);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Default child updated successfully", "data", child));
    }
}
