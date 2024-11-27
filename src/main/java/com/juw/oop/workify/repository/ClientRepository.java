package com.juw.oop.workify.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.juw.oop.workify.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    
}
