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
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namePermission;

    private String endPoint;

    private String permissionGr;

    @ManyToOne
    @JoinColumn(name = "function_id")
    @JsonIgnore
    private Function function ;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.MERGE},
            mappedBy = "permissions")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

}
