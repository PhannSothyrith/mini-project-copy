package org.example.model.response;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class UserResponse {
    //uuid
    private UUID userId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String lastModifiedAt;


}
