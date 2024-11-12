package com.juw.oop.workify.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.service.ClientService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

@Controller // handles HTTP requests & is used to create web controllers that return views (html pages), which is further resolved by view resolver (thymeleaf)

// This class will deals with actions related to client (creating, updating, deleting client profiles, etc.)
public class ClientController {
    @Autowired
    ClientService clientService;
    /* Spring will now look for bean of type ClientService and since ClientService is defined as 
    a Spring component (annotated with Service), Spring will automatically provide (or inject) 
    an instance of ClientService class into the class */

    // GetMapping = retrieve data, PostMapping = create/add data, DeleteMapping = delete data, PutMapping = update data


    @GetMapping("/client-signup")
    public String showSignUpPg(Model model) {
        model.addAttribute("client", new Client());
        return "/client/client-signup";
    }

    @PostMapping("/client-signup")
    public String registerClient(@Valid Client client, BindingResult result, Model model) {
        // If there are validation errors, bind the object to model and show sign up page again with the errors
        if (result.hasErrors()) {
            model.addAttribute("client", client); 
            return "/client/client-signup"; // Return the form with validation errors
        }

        Optional<String> clientExistsError = clientService.saveClient(client);

        if (clientExistsError.isPresent()) {
            model.addAttribute("error", clientExistsError.get()); 
            return "/client/client-signup"; 
        }

        return "/client/signup-success"; // Redirect to success page
    }
    @PutMapping("/clients/{id}")
    public ResponseEntity<String> updateClient(@Valid @RequestBody Client client, @PathVariable("id") Long clientId) {
        return clientService.updateClient(client, clientId); 
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable("id") Long clientId) {
        return clientService.deleteClientById(clientId);
    }

    @GetMapping("/clients")
    public String fetchClients(Model model) {
        List<Client> clientList = clientService.fetchClients();
        model.addAttribute("clientList", clientList);
        return "/client/list-of-clients";
    }
}
