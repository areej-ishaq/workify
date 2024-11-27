package com.juw.oop.workify.repository;
import com.juw.oop.workify.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findBySkillsRequiredIn(List<String> skills); // Use 'In' for a list of skills
    List<Work> findBySkillsRequiredContaining(String skill); // This is valid if skillsRequired is a collection
    List<Work> findBySkillsRequired(String skill); // If skillsRequired is a single string
}

