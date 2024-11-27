package com.juw.oop.workify.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> skillsRequired;
    private String title;
    private String description;
    private String clientEmail;
    @ElementCollection
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequiredSkills() {
        return skillsRequired;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.skillsRequired = skillsRequired;
    }


    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}