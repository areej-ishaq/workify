package com.juw.oop.workify.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.DocFlavor.STRING;

import com.juw.oop.workify.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.repository.ClientRepository;
import com.juw.oop.workify.service.ClientService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller // handles HTTP requests & is used to create web controllers that return views (html pages), which is further resolved by view resolver (thymeleaf)

// This class will deals with actions related to client (creating, updating, deleting client profiles, etc.)
public class ClientController {
    @Autowired
    ClientService clientService;
    private final FreelancerService freelancerService;
    /* Spring will now look for bean of type ClientService and since ClientService is defined as 
    a Spring component (annotated with Service), Spring will automatically provide (or inject) 
    an instance of ClientService class into the class */
    @Autowired
    ClientRepository clientRepository;

    public ClientController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping("/find-work")
    public String showFindWorkPage(HttpSession session, Model model) {
        Client clientRecord = (Client) session.getAttribute("client");

        model.addAttribute("client", clientRecord);
        return "/client/find-work";

    }
    
    @GetMapping("/find-work-results")
    public String showMatchingFreelancers(HttpSession session, Model model) {
        List<Freelancer> matchingFreelancers = new ArrayList<>();
        Client clientRecord = (Client) session.getAttribute("client");
        String skillRequirement = clientRecord.getSkillRequirement();

        if (skillRequirement != null && !skillRequirement.isEmpty()) {
            // Fetch freelancers matching the skillRequirement
            matchingFreelancers = freelancerService.findFreelancersBySkill(skillRequirement);
        }

        model.addAttribute("freelancers", matchingFreelancers);
        model.addAttribute("client", clientRecord); // Retain the skillRequirement for display
        return "/client/find-work-results"; // Points to find-work-results.html
    }
    
    // Show Sign Up page
    @GetMapping("/client-signup")
    public String showSignUpPg(Model model) {
        model.addAttribute("client", new Client());
        return "/client/client-signup";
    }
    
    @PostMapping("/client-signup")
    public String registerClient(@Valid @ModelAttribute Client client, 
                                BindingResult result, Model model, HttpSession session) {
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

        session.setAttribute("client", client);
        return "redirect:/client-home"; // Redirect to client dashboard page
    }

    // Show Login Page
    @GetMapping("/client-login")
    public String showLoginPage(Model model) {
        model.addAttribute("client", new Client());
        return "/client/client-login";  
    }

    @PostMapping("/client-login")
    public String clientLogin(@ModelAttribute Client client, HttpSession session, Model model) {
        Optional<Client> clientRecord = clientService.authenticateClient(client.getEmail(), client.getPassword());
        
        if (clientRecord.isPresent()) {
            // Store client object in session
            session.setAttribute("client", clientRecord.get());
            return "redirect:/client-home"; // Redirect to client home
        } else {
            // Invalid login
            model.addAttribute("error", "Invalid password or email");
            return "/client/client-login"; 
        }
    }
    
    @GetMapping("/client-home")
    public String showClientHomePg(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        String Clientname = client.getName();
        model.addAttribute("name", Clientname);
        model.addAttribute("client", client);
        return "/client/client-home";
    }
    
    @GetMapping("/update-skill-req/{email}")
    public String showUpdateSkillReqPg(@PathVariable String email, HttpSession session, Model model) {
        Client client = clientRepository.findByEmail(email).get();

        model.addAttribute("client", client);
        return "/client/update-skill-req";
    }

    @PutMapping("/update-skill-req/{email}")
    public String updateClient(@ModelAttribute Client client, 
                                @PathVariable("email") String email, Model model, 
                                HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<String> clientNotFoundError = clientService.updateSkillRequirement(client);

        if (clientNotFoundError.isPresent()) {
            model.addAttribute("error", clientNotFoundError.get());
            return "/client/update-skill-req";
        }

        session.setAttribute("client", client);
        redirectAttributes.addFlashAttribute("message", "Requirement successfully updated!");
        return "redirect:/client-home";
        
    } 

    /*  
    @PutMapping("/clients/{id}")
    public ResponseEntity<String> updateClient(@Valid @RequestBody Client client, @PathVariable("id") Long clientId) {
        return clientService.updateClient(client, clientId); 
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable("id") Long clientId) {
        return clientService.deleteClientById(clientId);
    }

    */
    @GetMapping("/clients")
    public String fetchClients(Model model) {
        List<Client> clientList = clientService.fetchClients();
        model.addAttribute("clientList", clientList);
        return "/client/list-of-clients";
    }
}
