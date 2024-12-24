package com.yash.enotes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Integer id;

    private String name;

    private String email;

    private String password;
    private String address;
    private char gender;
    private String role;
    private boolean isActive=true;
    private LocalDateTime createdAt;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Note> notes;
}
