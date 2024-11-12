package com.juw.oop.workify.entity;

// Code reference: https://www.geeksforgeeks.org/spring-boot-with-h2-database/

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Makes the class a database entity (a table)
@Data // Lombok annotation: Generates getters, setters, toString(), equals(), and hashCode() automatically.
@NoArgsConstructor // Lombok: Creates a no-argument constructor (empty constructor).
@AllArgsConstructor // Creates a constructor with all fields as parameters.
@Builder // Provides a flexible builder to create objects step-by-step.
@Table(name = "client") // Specify a different table name to avoid conflict as user is used by many SQL databases

// Create a POJO class ( https://www.geeksforgeeks.org/pojo-vs-java-beans/ )
public class Client {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) // Automatically assign a unique value to ID when a new record is saved
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 40, message = "Name should be between 3 - 40 characters.")
    private String name; 

    @NotBlank(message = "Email is required.")
    @Email
    private String email; 

    @NotBlank(message = "Skill requirement cannot be blank.")
    @Size(min = 3, max = 50, message = "Skill requirement must be between 3 and 50 characters.")
    private String skillRequirement;
    
    @NotBlank(message = "Location cannot be blank.")
    @Size(min = 4, max = 100, message = "Location must be between 2 and 100 characters.")
    private String location;

}
