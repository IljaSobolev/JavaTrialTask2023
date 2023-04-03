package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.error.VehicleTypeForbiddenException;
import com.example.demo.error.InvalidParameterException;
import com.example.demo.error.NoDataException;

@RestControllerAdvice
public class ApiExceptionHandler {
    /**
     * <p>Handles the {@link InvalidParameterException} exception</p>
     * @param e the exception
     * @return the string containing the requested parameter's name and value
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> handleApiException(InvalidParameterException e) {
        return new ResponseEntity<>("Invalid value '" + e.getValue() + "' of parameter '" + e.getName() + "'", HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>Handles the {@link VehicleTypeForbiddenException} exception</p>
     * @param e the exception
     * @return the string containing the requested vehicle type
     */
    @ExceptionHandler(VehicleTypeForbiddenException.class)
    public ResponseEntity<String> handleApiException(VehicleTypeForbiddenException e) {
        return new ResponseEntity<>("Forbidden vehicle type '" + e.getName() + "' for current weather conditions", HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>Handles the {@link NoDataException} exception</p>
     * @param e the exception
     * @return error message
     */
    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<String> handleApiException(NoDataException e) {
        return new ResponseEntity<>("The database has no observation records", HttpStatus.NOT_FOUND);
    }
}
