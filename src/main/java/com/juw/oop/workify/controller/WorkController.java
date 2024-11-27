package com.juw.oop.workify.controller;

import com.juw.oop.workify.model.Job;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkController {

    private List<Job> jobList = new ArrayList<>();

    public WorkController() {
        // Sample job data
        jobList.add(new Job("Content Writer", "Write engaging content for blogs.", "content writing"));
        jobList.add(new Job("Graphic Designer", "Create stunning graphics.", "graphic design"));
        jobList.add(new Job("SEO Specialist", "Optimize website content for search engines.", "SEO"));
        jobList.add(new Job("Content Editor", "Edit and proofread written content.", "content writing"));
    }

    @GetMapping("/findwork")
    public String findWork() {
        return "findwork"; // This will render findwork.html
    }

    @PostMapping("/jobs")
    public String getJobs(@RequestParam("skill") String skill, Model model) {
        List<Job> filteredJobs = new ArrayList<>();
        for (Job job : jobList) {
            if (job.getSkill().equalsIgnoreCase(skill)) {
                filteredJobs.add(job);
            }
        }
        model.addAttribute("jobs", filteredJobs);
        return "joblist"; // This will render joblist.html
    }

    @PostMapping("/acceptJob")
    public String acceptJob(@RequestParam("jobTitle") String jobTitle, Model model) {
        model.addAttribute("message", "You have accepted the job: " + jobTitle);
        return "findwork"; // Redirect back to findwork page
    }
}