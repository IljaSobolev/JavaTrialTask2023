package com.example.demo.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FeeCalculationService;
import com.example.demo.error.InvalidParameterException;
import com.example.demo.error.VehicleTypeForbiddenException;
import com.example.demo.error.NoDataException;

@RestController
public class FeeController {
    @Autowired
    private FeeCalculationService feeService;

    /**
     *  <p>Calculate delivery fee based on the weather in a given city and the vehicle type. Accessible at /fee/{city}/{type}</p>
     *  @param city the city to get weather conditions in
     *  @param vehicle the vehicle type
     *  @throws NoDataException if the database hasn't collected any weather observations
     *  @throws InvalidParameterException one of the parameters is invalid
     *  @throws VehicleTypeForbiddenException if the current weather conditions in city forbid the use of requested vehicle
     */
    @GetMapping(value = "/fee/{city}/{vehicle}")
    public BigDecimal getFee(@PathVariable String city, @PathVariable String vehicle) throws NoDataException, InvalidParameterException, VehicleTypeForbiddenException {
        return feeService.calculateFee(city, vehicle);
    }
}
