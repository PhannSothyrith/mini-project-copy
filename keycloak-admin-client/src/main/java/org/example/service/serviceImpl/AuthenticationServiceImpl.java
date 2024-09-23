package org.example.service.serviceImpl;
import jakarta.ws.rs.core.Response;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.model.dto.request.UserRequest;
import org.example.model.response.UserResponse;
import org.example.service.AuthenticationService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;
    public AuthenticationServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }
    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        UserRepresentation representation = prepareUserRepresentation(userRequest, preparePasswordRepresentation(userRequest.getPassword()));
        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(representation);

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ConflictException("This email is already registered");
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = usersResource.get(userId);
        UserRepresentation createdUser = userResource.toRepresentation();

        return prepareUserResponse(createdUser);
    }
    private UserRepresentation prepareUserRepresentation(UserRequest userRequest, CredentialRepresentation credentialRepresentation) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userRequest.getUsername());
        userRepresentation.setEmail(userRequest.getEmail());
        userRepresentation.setFirstName(userRequest.getFirstName());
        userRepresentation.setLastName(userRequest.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.singleAttribute("createdAt", String.valueOf(LocalDateTime.now()));
        userRepresentation.singleAttribute("lastModifiedAt", String.valueOf(LocalDateTime.now()));
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }
    private CredentialRepresentation preparePasswordRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }
    @Override
    public List<UserResponse> getAllUsers() {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> userRepresentations = usersResource.list();
        return userRepresentations.stream()
                .map(this::prepareUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation userRepresentation = usersResource.get(String.valueOf(userId)).toRepresentation();
        if (userRepresentation == null){
            throw  new NotFoundException("User not found");
        }
        return prepareUserResponse(userRepresentation);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> userRepresentations = usersResource.search(email);
        if (userRepresentations.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        UserRepresentation userRepresentation = userRepresentations.getFirst();
        return prepareUserResponse(userRepresentation);
    }

    @Override
    public UserResponse updateUserById(UUID userId, UserRequest userRequest) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId.toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (userRepresentation == null) {
            throw new NotFoundException("User not found with ID: " + userId);
        }

        // Update user fields based on the UserRequest
        userRepresentation.setFirstName(userRequest.getFirstName());
        userRepresentation.setLastName(userRequest.getLastName());
        userRepresentation.setEmail(userRequest.getEmail());
        userRepresentation.setUsername(userRequest.getUsername());
        userRepresentation.singleAttribute("createdAt", String.valueOf(LocalDateTime.now()));
        userRepresentation.singleAttribute("lastModifiedAt", String.valueOf(LocalDateTime.now()));
        userResource.update(userRepresentation);
        return prepareUserResponse(userRepresentation);
    }

    @Override
    public void deleteUserById(UUID userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId.toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (userRepresentation == null) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        userResource.remove();
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> userRepresentations = usersResource.search(username);
        if (userRepresentations.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        UserRepresentation userRepresentation = userRepresentations.getFirst();
        return prepareUserResponse(userRepresentation);
    }

    private UserResponse prepareUserResponse(UserRepresentation userRepresentation) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(UUID.fromString(userRepresentation.getId()));
        userResponse.setUserName(userRepresentation.getUsername());
        userResponse.setEmail(userRepresentation.getEmail());
        userResponse.setFirstName(userRepresentation.getFirstName());
        userResponse.setLastName(userRepresentation.getLastName());
        userResponse.setCreatedAt(userRepresentation.getAttributes().get("createdAt").getFirst());
        userResponse.setLastModifiedAt(userRepresentation.getAttributes().get("lastModifiedAt").getFirst());
        return userResponse;
    }
}
