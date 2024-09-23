package org.example.model.entity;


import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String createdAt;
    private String lastModifiedAt;


}
