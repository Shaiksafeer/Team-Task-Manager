package com.teammanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teammanager.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Project> createdProjects = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<ProjectMember> projectMemberships = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Task> assignedTasks = new ArrayList<>();
}
