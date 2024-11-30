package com.juw.oop.workify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientName;
    private String freelancerName;
    private String skill;
    private double price;
    private String status;
    
    @ManyToOne
    private Client client;

    @ManyToOne
    private Freelancer freelancer;

}

