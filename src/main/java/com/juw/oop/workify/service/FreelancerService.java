package com.juw.oop.workify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.repository.FreelancerRepository;

// ResponseEntity reference = https://www.baeldung.com/spring-response-entity - No longer in use

@Validated
@Service
public class FreelancerService {
    @Autowired
    private final FreelancerRepository freelancerRepository;

    public FreelancerService(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    public List<String> getAllDistinctSkills() {
        return freelancerRepository.findDistinctSkills();
    }

    public Optional<String> saveFreelancer(Freelancer freelancer) {
        // Check if a freelancer account with that email already exists
        Optional<Freelancer> existingFreelancer =  freelancerRepository.findByEmail(freelancer.getEmail());

        // If yes, then return message or else, return empty/null value
        if (existingFreelancer.isPresent()) {
            return Optional.of("Freelancer with that email already exists.");
        }
        else {
            freelancerRepository.save(freelancer);
            return Optional.empty();
        }
    }

    public List<Freelancer> fetchFreelancers() {
        return (List<Freelancer>) freelancerRepository.findAll();
    }

    public Optional<String> updateSkill(Freelancer freelancer) {
        Optional<Freelancer> freelancerRecordOptional = freelancerRepository.findByEmail(freelancer.getEmail());
    
        if (freelancerRecordOptional.isPresent()) {
            Freelancer freelancerRecord = freelancerRecordOptional.get();
            freelancerRecord.setSkill(freelancer.getSkill());
            //freelancerRecord.setName(freelancerRecord.getName());
            //freelancerRecord.setEmail(freelancerRecord.getEmail());
            //freelancerRecord.setLocation(freelancerRecord.getLocation());

            freelancerRepository.save(freelancerRecord);
            return Optional.empty();
        }
        else {
            return Optional.of("No freelancer with that email exists.");
        }
    }
    

    public ResponseEntity<String> deleteFreelancer(Long id) {
    Optional<Freelancer> freelancerRecord = freelancerRepository.findById(id);

    if (freelancerRecord.isPresent()) {
        freelancerRepository.deleteById(id);
        return ResponseEntity.ok("Freelancer successfully deleted");
    } 
    else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Freelancer not found");
    }
    }
}
