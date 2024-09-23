package org.example.service;


import org.example.model.entity.User;
import org.example.model.dto.request.UserRequest;
import org.example.model.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface AuthenticationService {

   UserResponse registerUser(UserRequest userRequest);
   List<UserResponse> getAllUsers();
   UserResponse getUserById(UUID userId);
   UserResponse getUserByEmail(String email);
   UserResponse updateUserById(UUID userId, UserRequest userRequest);
   void deleteUserById(UUID userId);
   UserResponse getUserByUsername(String username);
}
