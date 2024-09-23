package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.model.dto.request.UserRequest;
import org.example.model.response.UserResponse;
import org.example.service.AuthenticationService;
import org.example.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@SecurityRequirement(name = "realm-2")
@RequestMapping("api/v1/user/users")
public class UserController {
    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    @Operation(summary = "Register with your information")
    public ResponseEntity<APIResponse<UserResponse>> register (@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = authenticationService.registerUser(userRequest);
        APIResponse<UserResponse> apiResponse = APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED)
                .message("User registration")
                .payload(userResponse)
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = authenticationService.getAllUsers();
        APIResponse<List<UserResponse>> response = APIResponse.<List<UserResponse>>builder()
                .status(HttpStatus.CREATED)
                .message("User registration")
                .payload(users)
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }
    @GetMapping("/{userId}")
        public ResponseEntity<APIResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse user = authenticationService.getUserById(userId);
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .status(HttpStatus.OK)
                .message("User registration")
                .payload(user)
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<APIResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
    UserResponse user = authenticationService.getUserByEmail(email);
    APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
            .status(HttpStatus.OK)
            .message("User registration")
            .payload(user)
            .time(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponse>> updateUserById(@PathVariable String userId, @RequestBody UserRequest userRequest){
        UserResponse userResponse = authenticationService.updateUserById(UUID.fromString(userId), userRequest);
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
               .status(HttpStatus.OK)
               .message("User registration")
               .payload(userResponse)
               .time(LocalDateTime.now())
               .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponse>> deleteUserById(@PathVariable String userId){
        authenticationService.deleteUserById(UUID.fromString(userId));
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
               .status(HttpStatus.OK)
               .message("User registration")
               .time(LocalDateTime.now())
               .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("username/{username}")
    public ResponseEntity<APIResponse<UserResponse>> getUserByUsername(@PathVariable String username){
        UserResponse user = authenticationService.getUserByUsername(username);
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
               .status(HttpStatus.OK)
               .message("Get user by username is successfully")
               .payload(user)
               .time(LocalDateTime.now())
               .build();
        return ResponseEntity.ok(response);
    }
}
