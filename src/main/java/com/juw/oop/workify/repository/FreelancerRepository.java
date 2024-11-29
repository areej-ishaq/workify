package com.juw.oop.workify.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.juw.oop.workify.entity.Freelancer;

@Repository
public interface FreelancerRepository extends CrudRepository<Freelancer, Long> {

    // Method to find Freelancer by Email
    Optional<Freelancer> findByEmail(String email);

    // Method to find Freelancer by Email and Password
    Optional<Freelancer> findByEmailAndPassword(String email, String password);

    @Query("SELECT f FROM Freelancer f WHERE LOWER(f.skill) LIKE :skillPattern")
    List<Freelancer> findFreelancersBySkill(@Param("skillPattern") String skillPattern);
    
    // Method to find distinct skills from Freelancer entities
    @Query("SELECT DISTINCT f.skill FROM Freelancer f")
    List<String> findDistinctSkills();
}
