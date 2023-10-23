package com.example.activitytracker.service;

import com.example.activitytracker.dto.requestDto.UserDto;
import com.example.activitytracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    User RegisterUser(UserDto userDto);
    Boolean loginUser(String email, String password);
    void updateUser(Long id, UserDto userDto);
    void deleteUser(Long id, String email);
}
