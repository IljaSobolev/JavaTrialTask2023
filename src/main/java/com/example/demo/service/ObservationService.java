package com.example.demo.service;

import java.util.List;
import java.util.HashMap;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.ObservationStoreService;
import com.example.demo.util.ObservationFetcher;
import com.example.demo.util.ObservationFetcherEEA;
import com.example.demo.modal.Observation;

/**
 * Fetches weather observations and adds them to a database
 */
@Service
public class ObservationService {
    @Autowired
    private ObservationStoreService storeService;

    private ObservationFetcher fetcher = new ObservationFetcherEEA();

    /**
     *  <p>Return the number of observations</p>
     *  @return the number of observations in the database
     */
    public long count() {
        return storeService.count();
    }

    /**
     *  <p>Return all observations as a list</p>
     *  @return list of all observations present in the database
     */
    public List<Observation> findAll() {
        return storeService.findAll();
    }

    /**
     *  <p>Fetches the weather records and stores the required records into the database</p>
     */
    public void fetchAndStore() {
        if(fetcher == null || storeService == null) return;

        HashMap<String, Observation> observations = fetcher.fetch();
        storeService.save(observations.get("Tallinn-Harku"));
        storeService.save(observations.get("Tartu-Tõravere"));
        storeService.save(observations.get("Pärnu"));
    }

    /**
     *  <p>Return the weather record for a given city that is the closest in time to the given timestamp</p>
     *  @param timestamp the timestamp
     *  @param city the city name
     *  @return observation object corresponding to input parameters
     */
    public Observation getNearestByCity(Timestamp timestamp, String city) {
        return storeService.getNearestByCity(timestamp, city);
    }
}
