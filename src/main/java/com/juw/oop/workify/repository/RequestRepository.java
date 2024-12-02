package com.juw.oop.workify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByFreelancer(Freelancer freelancer);
    List<Request> findByClient(Client client);
}
