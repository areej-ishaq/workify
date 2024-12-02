package com.juw.oop.workify.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.juw.oop.workify.service.FreelancerService;
import com.juw.oop.workify.service.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.entity.Request;
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
    @Autowired
    RequestService requestService;

    public ClientController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
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
            // Fetch the existing client from the session (if any)
            Client existingClient = (Client) session.getAttribute("client");

            if (existingClient != null) {
                existingClient.setId(clientRecord.get().getId());
                existingClient.setName(clientRecord.get().getName());
                existingClient.setLocation(clientRecord.get().getLocation());
                existingClient.setEmail(clientRecord.get().getEmail());

                System.out.println("client id: " + existingClient.getId());
                // Save the updated client to the session
                session.setAttribute("client", existingClient);
        } else {
            // If no existing client in the session, set the authenticated client in the session
            session.setAttribute("client", clientRecord.get());
        }
            return "redirect:/client-home"; // Redirect to client home
        } else {
            // Invalid login
            model.addAttribute("error", "Invalid password or email");
            return "/client/client-login"; 
        }
    }
    
    // Show client dashboard
    @GetMapping("/client-home")
    public String showClientHomePg(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        String Clientname = client.getName();
        Boolean nullClientName = false; // Check if client name is null (if they directly access client-home link)

        if (Clientname == null) {
            nullClientName = true;
        }
        model.addAttribute("name", Clientname);
        model.addAttribute("client", client);
        model.addAttribute("nullClient", nullClientName);
        return "/client/client-home";
    }
    
    @GetMapping("/find-work")
    public String showFindWorkPage(HttpSession session, Model model) {
        Client clientRecord = (Client) session.getAttribute("client");

        model.addAttribute("client", clientRecord);
        return "/client/find-work";

    }
    
    // View results of matching freelancers
    @GetMapping("/find-work-results")
    public String showMatchingFreelancers(HttpSession session, Model model) {
        List<Freelancer> matchingFreelancers = new ArrayList<>();
        Client clientRecord = (Client) session.getAttribute("client");
        String skillRequirement = clientRecord.getSkillRequirement();
        Boolean noFreelancersFound = false;

        if (skillRequirement != null && !skillRequirement.isEmpty()) {
            // Fetch freelancers matching the skillRequirement
            matchingFreelancers = freelancerService.findFreelancersBySkill(skillRequirement);
        }
        if (matchingFreelancers.isEmpty()) {
            noFreelancersFound = true;
        }

        model.addAttribute("freelancers", matchingFreelancers);
        model.addAttribute("client", clientRecord); // Retain the skillRequirement for display
        model.addAttribute("noFreelancersFound", noFreelancersFound);
        return "/client/find-work-results"; // Points to find-work-results.html
    }

    // Send request
    @GetMapping("/send-request/{freelancerId}")
    public String sendRequest(@PathVariable Long freelancerId, HttpSession session, RedirectAttributes redirectAttributes) 
    {
        Client client = (Client) session.getAttribute("client");

        if (client == null) {
            redirectAttributes.addFlashAttribute("error", "Client information is missing.");
            return "redirect:/client/find-work";
        }
        
        // Fetch the client from the database to ensure it's managed
        Client managedClient = clientService.findById(client.getId());
        if (managedClient == null) {
            redirectAttributes.addFlashAttribute("error", "Client not found in the database.");
            return "redirect:/client/find-work";
        }
        
        Freelancer freelancer = freelancerService.findById(freelancerId);
        if (freelancer == null) {
            redirectAttributes.addFlashAttribute("error", "Freelancer not found.");
            return "redirect:/client/find-work";
        }
        
        Request request = new Request();
        request.setClient(managedClient); // Use the managed client
        request.setFreelancer(freelancer);
        request.setClientName(managedClient.getName());
        request.setFreelancerName(freelancer.getName());
        request.setPrice(freelancer.getPrice());
        request.setSkill(freelancer.getSkill());
        request.setStatus("Pending");
        
        requestService.saveRequest(request);
        
        redirectAttributes.addFlashAttribute("message", "Request sent successfully!");
        return "redirect:/client-home";
    }

        // View status of requests sent 
    @GetMapping("/view-requests-status")
    public String showRequestsStatuspg(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        Boolean noRequests = false;

        if (client == null) {
            return "redirect:/client-login";
        }

        List<Request> requests = requestService.findByClient(client);
        if (requests.isEmpty()) {
            noRequests = true;
        }
        model.addAttribute("requests", requests);
        model.addAttribute("noRequests", noRequests);
        return "/client/view-requests-status";
    }

    @GetMapping("/update-skill-req/{email}")
    public String showUpdateSkillReqPg(@PathVariable String email, HttpSession session, Model model) 
    {
        Client client = clientRepository.findByEmail(email).get();

        model.addAttribute("client", client);
        return "/client/update-skill-req";
    }

    @PutMapping("/update-skill-req/{email}")
    public String updateClient(@ModelAttribute Client client, 
                                @PathVariable("email") String email, Model model, 
                                HttpSession session, RedirectAttributes redirectAttributes) 
    {
        Client existingClient = (Client) session.getAttribute("client");
        existingClient.setSkillRequirement(client.getSkillRequirement());
        session.setAttribute("client", existingClient);

        Optional<String> clientNotFoundError = clientService.updateSkillRequirement(client);

        if (clientNotFoundError.isPresent()) {
            model.addAttribute("error", clientNotFoundError.get());
            return "/client/update-skill-req";
        }

        redirectAttributes.addFlashAttribute("message", "Requirement successfully updated!");
        return "redirect:/client-home";
        
    }
    
}
