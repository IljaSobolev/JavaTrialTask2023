package com.example.demo.util;

import java.util.HashMap;

import com.example.demo.modal.Observation;

/**
  * Represents classes that would fetch weather observation data from different sources
  */
public interface ObservationFetcher {
    /**
      * Gets and returns the weather observations.
      * @return a hashmap mapping cities to weather observations.
      */
    public HashMap<String, Observation> fetch();
}
