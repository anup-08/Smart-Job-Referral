package com.smartJob.entity;

import com.smartJob.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 100, allocationSize = 1)
    private Long id;

    private String name;
    @Column(unique = true , nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
