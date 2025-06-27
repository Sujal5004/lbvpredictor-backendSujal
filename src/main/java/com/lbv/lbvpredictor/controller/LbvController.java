package com.lbv.lbvpredictor.controller;

import com.lbv.lbvpredictor.service.LbvService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lbv")
@CrossOrigin
public class LbvController {

    private final LbvService lbvService;

    public LbvController(LbvService lbvService) {
        this.lbvService = lbvService;
    }

    @GetMapping("/predict")
    public double predict(@RequestParam String fuelType,
                          @RequestParam double temperature,
                          @RequestParam double pressure,
                          @RequestParam double equivalenceRatio) {
        return lbvService.predict(temperature, pressure, fuelType, equivalenceRatio);
    }
}
