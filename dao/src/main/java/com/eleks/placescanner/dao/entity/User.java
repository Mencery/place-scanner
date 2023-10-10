package com.eleks.placescanner.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("user")
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String role;

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

}
