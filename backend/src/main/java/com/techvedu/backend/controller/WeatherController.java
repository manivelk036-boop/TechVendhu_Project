package com.techvedu.backend.controller;

import com.techvedu.backend.dto.ForecastResponse;
import com.techvedu.backend.dto.WeatherResponse;
import com.techvedu.backend.exception.BadRequestException;
import com.techvedu.backend.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam(value = "city", required = false) String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new BadRequestException("Query parameter 'city' must not be empty");
        }
        WeatherResponse resp = weatherService.getWeatherByCity(city.trim());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastResponse>> getForecast(@RequestParam(value = "city", required = false) String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new BadRequestException("Query parameter 'city' must not be empty");
        }
        List<ForecastResponse> forecast = weatherService.getForecastByCity(city.trim());
        return ResponseEntity.ok(forecast);
    }
}
