package com.lbv.lbvpredictor.model;

public class DataPoint {
    private double temperature;
    private double pressure;
    private String fuelType;
    private double equivalenceRatio;
    private double lbv;

    public DataPoint(double temperature, double pressure, String fuelType, double equivalenceRatio, double lbv) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.fuelType = fuelType;
        this.equivalenceRatio = equivalenceRatio;
        this.lbv = lbv;
    }

    public double getTemperature() { return temperature; }
    public double getPressure() { return pressure; }
    public String getFuelType() { return fuelType; }
    public double getEquivalenceRatio() { return equivalenceRatio; }
    public double getLbv() { return lbv; }
}

