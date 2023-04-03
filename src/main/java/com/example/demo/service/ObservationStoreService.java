package com.example.demo.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

import com.example.demo.repository.ObservationRepository;
import com.example.demo.modal.Observation;

/**
  * Class for performing actions on the database.
  */
@Service
public class ObservationStoreService {
    @Autowired
    private ObservationRepository observationRepository;

    public ObservationStoreService(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    /**
     *  <p>Return the number of weather records.</p>
     *  @return the number of weather records
     */
    public long count() {
        return observationRepository.count();
    }

    /**
     *  <p>Return all weather records as a list</p>
     *  @return all weather records present in the database
     */
    public List<Observation> findAll() {
        return observationRepository.findAll();
    }

    /**
     *  <p>Return the weather record for a given city that is the closest in time to the given timestamp</p>
     *  @param timestamp the timestamp
     *  @param city the city name
     *  @return observation object corresponding to input parameters
     */
    public Observation getNearestByCity(Timestamp timestamp, String city) {
        return observationRepository.findAll().stream()
                                              .filter(s -> s.getName().equals(city))
                                              .min(Comparator.comparing(s -> Math.abs(s.getTimestamp().getTime() - timestamp.getTime())))
                                              .get();
    }

    /**
     *  <p>Adds the weather record to the database</p>
     *  @param observation the observation to add
     */
    public void save(Observation observation) {
        observationRepository.save(observation);
    }
}
