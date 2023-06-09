package com.example.demo.error;

public class VehicleTypeForbiddenException extends RuntimeException {
    private String name;

    public VehicleTypeForbiddenException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String name) {
        this.name = name;
    }
}
