package com.example.demojwt.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="functions")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameFunc;

    private String url;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade =  CascadeType.MERGE,
            mappedBy = "functions")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "function")
    @JsonIgnore
    private Set<Permission> permissions = new HashSet<>();
}
