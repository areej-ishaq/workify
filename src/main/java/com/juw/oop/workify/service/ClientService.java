package com.juw.oop.workify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.repository.ClientRepository;

// ResponseEntity reference = https://www.baeldung.com/spring-response-entity

@Validated // ensures validation for method parameters in a class
@Service
/* The service class contains business logic related to the entity (user). 
It interacts with both the controller to handle requests and the repository to 
perform database operations. */

public class ClientService {
    @Autowired // Automatically create UserRepository instance
    ClientRepository clientRepository;
    
    public Optional<String> saveClient(Client client) {
        Optional<Client> existingClient = clientRepository.findByEmail(client.getEmail());
        // Optional is a container object which may contain null or non-null value. 
        // With Optional<Client>, an Optional container that wraps Client object is created. Used to avoid null exceptions
        if (existingClient.isPresent()) {
            return Optional.of("Client with that email already exists"); // Return error message
        } 
        else {
            clientRepository.save(client);
            return Optional.empty();  // Return empty string meaning no error
        }
    }

    public List<Client> fetchClients() {
        return (List<Client>) clientRepository.findAll(); // Return all user records in database as a list
    }

    public ResponseEntity<String> deleteClientById(Long Id) {
        Optional<Client> clientRecord = clientRepository.findById(Id);

        if (clientRecord.isPresent()) {
            clientRepository.deleteById(Id);
            return ResponseEntity.ok("Client successfully deleted");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        
    }

    public Optional<String> updateSkillRequirement(Client client) {
        Optional<Client> clientRecordOptional = clientRepository.findByEmail(client.getEmail());

        if (clientRecordOptional.isPresent()) {
            Client clientRecord = clientRecordOptional.get();
            clientRecord.setSkillRequirement(client.getSkillRequirement());
            clientRecord.setName(clientRecord.getName());
            clientRecord.setEmail(clientRecord.getEmail());
            clientRecord.setLocation(clientRecord.getLocation());

            clientRepository.save(clientRecord);
            return Optional.empty();
        }
        else {
            return Optional.of("No client with that email exists.");
        }
    }

    public ResponseEntity<String> updateClient(Client client, Long Id) {
        Optional<Client> clientRecordOptional = clientRepository.findById(Id);

        if (clientRecordOptional.isPresent()) {
            Client clientRecord = clientRecordOptional.get();

            clientRecord.setName(client.getName());
            clientRecord.setEmail(client.getEmail());
            clientRecord.setLocation(client.getLocation());
            clientRecord.setSkillRequirement(client.getSkillRequirement());

            clientRepository.save(clientRecord);
            return ResponseEntity.ok("Client information successfully updated");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        
    }
}
