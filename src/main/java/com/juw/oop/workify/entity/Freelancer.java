package com.juw.oop.workify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Freelancer {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 40, message = "Name should be between 3 - 40 characters.")
    private String name; 

    @NotBlank(message = "Email is required.")
    @Email
    private String email; 

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Skill is required.")
    @Size(min = 3, max = 50, message = "Skill must be between 3 and 50 characters.")
    private String skill;

    @NotBlank(message = "Location cannot be blank.")
    @Size(min = 4, max = 100, message = "Location must be between 2 and 100 characters.")
    private String location;    
}