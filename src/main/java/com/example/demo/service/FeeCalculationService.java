package com.example.demo.service;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import com.example.demo.modal.Observation;
import com.example.demo.error.VehicleTypeForbiddenException;
import com.example.demo.error.InvalidParameterException;
import com.example.demo.error.NoDataException;
import com.example.demo.service.ObservationService;

/**
  * <p>Contains all business logic for fee calculation.</p>
  */
@Service
public class FeeCalculationService {
    static final List<String> cities = Arrays.asList("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
    static final List<String> vehicles = Arrays.asList("Car", "Scooter", "Bike");

    @Autowired
    private ObservationService observationService;

    private BigDecimal regionalBaseFee(String city, String vehicle) throws InvalidParameterException {
        if(city.equals("Tallinn-Harku")) {
            if(vehicle.equals("Car")) return new BigDecimal(4.0);
            else if(vehicle.equals("Scooter")) return new BigDecimal(3.5);
            else if(vehicle.equals("Bike")) return new BigDecimal(3.0);
        }
        else if(city.equals("Tartu-Tõravere")) {
            if(vehicle.equals("Car")) return new BigDecimal(3.5);
            else if(vehicle.equals("Scooter")) return new BigDecimal(3.0);
            else if(vehicle.equals("Bike")) return new BigDecimal(2.0);
        }
        else if(city.equals("Pärnu")) {
            if(vehicle.equals("Car")) return new BigDecimal(3.0);
            else if(vehicle.equals("Scooter")) return new BigDecimal(2.5);
            else if(vehicle.equals("Bike")) return new BigDecimal(2.0);
        }

        throw new InvalidParameterException("city", city);
    }

    private boolean isSnowOrSleet(String phenomenon) {
        return phenomenon.equals("Light sleet") ||
               phenomenon.equals("Moderate sleet") ||
               phenomenon.equals("Light snowfall") ||
               phenomenon.equals("Moderate snowfall") ||
               phenomenon.equals("Blowing snow") ||
               phenomenon.equals("Drifting snow") ||
               phenomenon.equals("Light snow shower") ||
               phenomenon.equals("Moderate snow shower") ||
               phenomenon.equals("Heavy snow shower");
    }

    private boolean isRain(String phenomenon) {
        return phenomenon.equals("Light rain") ||
               phenomenon.equals("Moderate rain") ||
               phenomenon.equals("Heavy rain") ||
               phenomenon.equals("Light shower") ||
               phenomenon.equals("Moderate shower") ||
               phenomenon.equals("Heavy shower");
    }

    private BigDecimal extraFee(String vehicle, Observation observation) throws VehicleTypeForbiddenException {
        BigDecimal atef = new BigDecimal(0.0);
        BigDecimal wsef = new BigDecimal(0.0);
        BigDecimal wpef = new BigDecimal(0.0);

        if(vehicle.equals("Scooter") || vehicle.equals("Bike")) {
            if(observation.getAirTemperature() < -10.0) atef = new BigDecimal(1.0);
            else if(observation.getAirTemperature() < 0.0) atef = new BigDecimal(0.5);
        }

        if(vehicle.equals("Bike")) {
            if(observation.getWindSpeed() > 20) throw new VehicleTypeForbiddenException(vehicle);
            else if(observation.getWindSpeed() > 10) wsef = new BigDecimal(0.5);
        }

        String phenomenon = observation.getWeatherPhenomenon();
        if(phenomenon != null && (vehicle.equals("Scooter") || vehicle.equals("Bike"))) {
            if(isSnowOrSleet(phenomenon)) wpef = new BigDecimal(1.0);
            else if(isRain(phenomenon)) wpef = new BigDecimal(0.5);
            else if(phenomenon.equals("Glaze") || phenomenon.equals("Hail") || phenomenon.equals("Thunder")) throw new VehicleTypeForbiddenException(vehicle);
        }

        BigDecimal extraFee = new BigDecimal(0.0);
        extraFee.add(atef).add(wsef).add(wpef);
        return extraFee;
    }

    /**
     *  <p>Calculates the delivery fee in euros for the current city and vehicle type</p>
     *  @param city the city
     *  @param vehicle the vehicle type
     *  @return the delivery fee
     *  @throws InvalidParameterException if the city or vehicle parameters are invalid
     */
    public BigDecimal calculateFee(String city, String vehicle) throws InvalidParameterException {
        if(observationService.count() == 0) throw new NoDataException();
        if(!cities.contains(city)) throw new InvalidParameterException("city", city);
        if(!vehicles.contains(vehicle)) throw new InvalidParameterException("vehicle", vehicle);

        Timestamp now = Timestamp.from(Instant.now());
        Observation observation = null;
        try {
            observation = observationService.getNearestByCity(now, city);
        }
        catch(NoSuchElementException e) {
            throw new InvalidParameterException("city", city);
        }

        return regionalBaseFee(city, vehicle).add(extraFee(vehicle, observation));
    }
}
