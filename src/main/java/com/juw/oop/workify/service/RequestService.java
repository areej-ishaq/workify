package com.juw.oop.workify.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juw.oop.workify.entity.Client;
import com.juw.oop.workify.entity.Freelancer;
import com.juw.oop.workify.entity.Request;
import com.juw.oop.workify.repository.RequestRepository;

@Service
public class RequestService{
    @Autowired
    RequestRepository requestRepository;

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    public List<Request> findByFreelancer(Freelancer freelancer) {
        return requestRepository.findByFreelancer(freelancer);
    }

    public Request findById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }

    public List<Request> findByClient(Client client) {
        return requestRepository.findByClient(client);
    }

}
