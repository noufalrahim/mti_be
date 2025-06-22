package com.tinysteps.tinysteps.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinysteps.tinysteps.model.UserModel;
import com.tinysteps.tinysteps.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping("")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Create a new user")
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserModel user) {
        return userService.addUser(user.getPhone());
    }

    @Operation(summary = "Edit an existing user")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> editUser(@PathVariable Long id, @RequestBody UserModel updatedUser) {
        return userService.editUser(id, updatedUser);
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Get Default Child")
    @GetMapping("/default-child/{id}")
    public ResponseEntity<Map<String, Object>> getDefaultChild(@PathVariable Long id) {
        return userService.getDefaultChild(id);
    }

    @Operation(summary = "Edit Default Child")
    @PutMapping("/{userId}/default-child/{childId}")
    public ResponseEntity<Map<String, Object>> editDefaultChild(@PathVariable Long userId, @PathVariable Long childId) {
        return userService.editDefaultChild(userId, childId);
    }
}
