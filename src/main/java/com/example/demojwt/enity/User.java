package com.example.demojwt.enity;


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
    private Long id;

    private String username;

    private String password;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"), referencedColumnName = "id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Student student;

}