package com.juw.oop.workify.model;

public class Job{
    private String title;
    private String description;
    private String skill;

    public Job(String title, String description, String skill) {
        this.title = title;
        this.description = description;
        this.skill = skill;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSkill() {
        return skill;
    }
}
