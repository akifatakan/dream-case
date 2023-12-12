package com.example.dreamcase.controller;


import com.example.dreamcase.model.User;
import com.example.dreamcase.request.CreateUserRequest;
import com.example.dreamcase.response.ResponseHandler;
import com.example.dreamcase.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUserRequest(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            User user = userService.createUser(createUserRequest);
            return ResponseHandler.responseSuccessBuilder("New User Created!", HttpStatus.CREATED, user);
        } catch (IllegalArgumentException error) {
            // Handle the case where the country is invalid
            String message = error.getMessage();
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            // Handle the unique constraint violation here
            return ResponseHandler.responseErrorBuilder("Username must be unique.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-level/{userId}")
    public ResponseEntity<Object> updateLevelRequest(@PathVariable Long userId){
        try {
            User user = userService.updateLevel(userId);
            return ResponseHandler.responseSuccessBuilder("User level updated successfully!", HttpStatus.OK, user);
        } catch (IllegalArgumentException error){
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException error) {
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception error) {
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            return ResponseHandler.responseSuccessBuilder("User retrieved successfully!", HttpStatus.OK, user);
        } catch (NoSuchElementException error) {
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllUsers(){
        try {
            List<User> userList = userService.getAllUsers();
            return ResponseHandler.responseSuccessBuilder("All Users retrieved successfully!", HttpStatus.OK, userList);
        } catch (Exception error) {
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
