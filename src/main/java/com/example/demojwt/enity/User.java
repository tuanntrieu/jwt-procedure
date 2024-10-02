package com.example.demojwt.enity;


import com.example.demojwt.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullname;

    private String username;

    private String password;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    private Role role;


}