package com.example.demo.repository;

import org.springframework.data.repository.ListCrudRepository;  
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Bean;

import com.example.demo.modal.Observation;

@Repository
public interface ObservationRepository extends ListCrudRepository<Observation, Integer> {  
}  
