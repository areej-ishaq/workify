package com.juw.oop.workify.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.juw.oop.workify.entity.Freelancer;

@Repository
public interface FreelancerRepository extends CrudRepository<Freelancer, Long> {
    public Optional<Freelancer> findByEmail(String email);
    
}
