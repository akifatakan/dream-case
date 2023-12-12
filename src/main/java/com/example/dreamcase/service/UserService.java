package com.example.dreamcase.service;

import com.example.dreamcase.model.User;
import com.example.dreamcase.request.CreateUserRequest;

import java.util.List;

public interface UserService {
    public User createUser(CreateUserRequest createUserRequest);


    public User updateLevel(Long userId);
    public User getUserById(Long userId);

    public List<User> getAllUsers();

}
