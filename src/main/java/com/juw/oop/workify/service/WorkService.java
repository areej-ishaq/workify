package com.juw.oop.workify.service;

import com.juw.oop.workify.entity.Work;
import com.juw.oop.workify.repository.WorkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    public List<Work> findWorkBySkill(String skill) {
        return workRepository.findBySkillsRequiredContaining(skill);
    }

    public String acceptWork(Long workId, String clientEmail) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        // Logic for accepting work can be added here

        // Return a message on accepting work
        return "You have accepted the work: " + work.getTitle();
    }
}