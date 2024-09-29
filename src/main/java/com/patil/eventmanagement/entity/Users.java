package com.patil.eventmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "First name can't be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "firstname")
    private String firstname;

    @NotNull(message = "Last name can't be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "lastname")
    private String lastname;

    @NotNull(message = "Email can't be null")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = "Password can't be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits long.")
    @Column(name = "contact_number")
    private String contactNumber;

    @NotNull(message = "Role can't be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERoles role;
}
