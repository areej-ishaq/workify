package com.juw.oop.workify.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.repository.FreelancerRepository;
import com.juw.oop.workify.service.FreelancerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class FreelancerController {
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    FreelancerRepository freelancerRepository;
    
    @GetMapping("/freelancer-signup")
    public String showSignUpPg(Model model) {
    model.addAttribute("freelancer", new Freelancer());
        return "/freelancer/freelancer-signup";
     }
    
    @PostMapping("/freelancer-signup")
    public String registerFreelancer(@Valid @ModelAttribute Freelancer freelancer, BindingResult result, 
                                    Model model, HttpSession session) {
        // If there are validation errors, bind the object to model and show sign up page again with the errors
        if (result.hasErrors()) {
            model.addAttribute("freelancer", freelancer);
            return "/freelancer/freelancer-signup";
        }

        // Check if any account with same email exists
        Optional<String> freelancerExistsError = freelancerService.saveFreelancer(freelancer);

        if (freelancerExistsError.isPresent()) {
            model.addAttribute("error", freelancerExistsError.get());
            return "/freelancer/freelancer-signup";
        }

        session.setAttribute("freelancer", freelancer);;
        return "redirect:/freelancer-home";
    }
    
    @GetMapping("/freelancer-home")
    public String showFreelancerHomePg(HttpSession session, Model model) {
        Freelancer freelancer = (Freelancer) session.getAttribute("freelancer");
        String name = freelancer.getName();

        model.addAttribute("name", name);
        model.addAttribute("freelancer", freelancer);

        return "/freelancer/freelancer-home";
    }

    @GetMapping("/update-skill/{email}")
    public String showUpdateSkillPg(@PathVariable String email, HttpSession session, Model model) {
        Freelancer freelancer = freelancerRepository.findByEmail(email).get();
        
        model.addAttribute("freelancer", freelancer);
        return "/freelancer/update-skill";
    }

    @PutMapping("/update-skill/{email}")
    public String updateSkill(@ModelAttribute Freelancer freelancer, 
                                @PathVariable("email") String email, Model model, 
                                HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<String> freelancerNotFoundError = freelancerService.updateSkill(freelancer);

        if (freelancerNotFoundError.isPresent()) {
            model.addAttribute("error", freelancerNotFoundError.get());
            return "/freelancer/update-skill";
        }

        redirectAttributes.addFlashAttribute("message", "Skill successfully updated!");
        return "redirect:/freelancer-home";
        
    } 
    /*  
    @PutMapping("/freelancers/{id}")
public ResponseEntity<String> updateFreelancer(@Valid @RequestBody Freelancer freelancer, 
                                               @PathVariable("id") Long freelancerId, 
                                               BindingResult bindingResult) {
    // Check if validation errors exist
    if (bindingResult.hasErrors()) {
        // Collect all error messages into a single string
        String errorMessages = bindingResult.getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        // Return a bad request response with the validation error messages
        return ResponseEntity.badRequest().body("Validation failed: " + errorMessages);
    }

    // If validation is successful, delegate to the service layer to update the freelancer
    return freelancerService.updateFreelancer(freelancer, freelancerId);
    }


    @DeleteMapping("/freelancers/{id}")
    public ResponseEntity<String> deleteFreelancer(@PathVariable("id") Long freelancerId) {
        return freelancerService.deleteFreelancer(freelancerId);
    }
*/
    @GetMapping("/freelancers")
    public String fetchFreelancers(Model model) {
        List<Freelancer> freelancersList = freelancerService.fetchFreelancers();
        model.addAttribute("freelancersList", freelancersList);
        return "/freelancer/list-of-freelancers";
    }
}
