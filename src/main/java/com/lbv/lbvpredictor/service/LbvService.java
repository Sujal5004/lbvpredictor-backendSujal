package com.lbv.lbvpredictor.service;

import com.lbv.lbvpredictor.model.DataPoint;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LbvService {
    private final List<DataPoint> dataset = new ArrayList<>();

    @PostConstruct
    public void loadCSV() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("Updated2024CUTEDnew.csv")) {
            if (input == null) throw new RuntimeException("CSV file not found");

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        double temperature = Double.parseDouble(parts[0].trim());
                        double pressure = Double.parseDouble(parts[1].trim());
                        String fuelType = parts[2].trim();
                        double equivalenceRatio = Double.parseDouble(parts[3].trim());
                        double lbv = Double.parseDouble(parts[4].trim());
                        dataset.add(new DataPoint(temperature, pressure, fuelType, equivalenceRatio, lbv));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load dataset", e);
        }
    }

    public double predict(double temperature, double pressure, String fuelType, double equivalenceRatio) {
        List<DataPoint> neighbors = dataset.stream()
            .filter(dp -> dp.getFuelType().equalsIgnoreCase(fuelType))
            .sorted(Comparator.comparingDouble(dp ->
                Math.pow(dp.getTemperature() - temperature, 2) +
                Math.pow(dp.getPressure() - pressure, 2) +
                Math.pow(dp.getEquivalenceRatio() - equivalenceRatio, 2)))
            .limit(3)
            .toList();

        if (neighbors.isEmpty()) {
            throw new RuntimeException("No matching data points found for fuel type: " + fuelType);
        }

        return lagrangeInterpolate(equivalenceRatio, neighbors);
    }

    private double lagrangeInterpolate(double x, List<DataPoint> points) {
        double result = 0.0;
        for (int i = 0; i < points.size(); i++) {
            double term = points.get(i).getLbv();
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    double xi = points.get(i).getEquivalenceRatio();
                    double xj = points.get(j).getEquivalenceRatio();
                    term *= (x - xj) / (xi - xj);
                }
            }
            result += term;
        }
        return result;
    }
}
