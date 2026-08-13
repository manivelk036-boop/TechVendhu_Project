package com.techvedu.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techvedu.backend.dto.ForecastResponse;
import com.techvedu.backend.dto.WeatherResponse;
import com.techvedu.backend.exception.CityNotFoundException;
import com.techvedu.backend.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String apiKey;

    public WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper,
                          @Value("${weather.base.url}") String baseUrl,
                          @Value("${weather.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public WeatherResponse getWeatherByCity(String city) {
        try {
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric", baseUrl, city, apiKey);
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new ExternalApiException("Invalid response from weather provider");
            }
            JsonNode root = objectMapper.readTree(resp.getBody());

            JsonNode main = root.path("main");
            JsonNode wind = root.path("wind");
            JsonNode sys = root.path("sys");
            JsonNode weatherArray = root.path("weather");
            JsonNode weather = weatherArray.isArray() && weatherArray.size() > 0 ? weatherArray.get(0) : null;

            if (weather == null || main.isMissingNode()) {
                throw new ExternalApiException("Unexpected API response structure");
            }

            String cityName = root.path("name").asText(city);
            String country = sys.path("country").asText("");
            double temp = main.path("temp").asDouble(0.0);
            double feelsLike = main.path("feels_like").asDouble(0.0);
            int humidity = main.path("humidity").asInt(0);
            double windSpeed = wind.path("speed").asDouble(0.0);
            String condition = weather.path("main").asText("");
            String description = weather.path("description").asText("");
            String icon = weather.path("icon").asText("");

            return new WeatherResponse(cityName, country, temp, feelsLike, humidity, windSpeed, condition, description, icon);

        } catch (HttpClientErrorException.NotFound nf) {
            throw new CityNotFoundException("City not found: " + city);
        } catch (HttpClientErrorException hce) {
            throw new ExternalApiException("Weather provider error: " + hce.getStatusCode(), hce);
        } catch (RestClientException rce) {
            throw new ExternalApiException("Failed to call weather provider", rce);
        } catch (Exception e) {
            throw new ExternalApiException("Failed to parse weather response", e);
        }
    }

    public List<ForecastResponse> getForecastByCity(String city) {
        try {
            String url = String.format("%s/forecast?q=%s&appid=%s&units=metric", baseUrl, city, apiKey);
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new ExternalApiException("Invalid response from weather provider");
            }
            JsonNode root = objectMapper.readTree(resp.getBody());

            JsonNode cityNode = root.path("city");
            String cityName = cityNode.path("name").asText(city);
            String country = cityNode.path("country").asText("");
            JsonNode list = root.path("list");

            if (!list.isArray() || list.isEmpty()) {
                throw new ExternalApiException("Unexpected API response structure");
            }

            List<ForecastResponse> forecastList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (JsonNode item : list) {
                JsonNode main = item.path("main");
                JsonNode wind = item.path("wind");
                JsonNode weatherArray = item.path("weather");
                JsonNode weather = weatherArray.isArray() && weatherArray.size() > 0 ? weatherArray.get(0) : null;

                if (weather == null || main.isMissingNode()) {
                    continue;
                }

                long epochSeconds = item.path("dt").asLong(0L);
                String dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault())
                        .format(formatter);

                String condition = weather.path("main").asText("");
                String description = weather.path("description").asText("");
                String icon = weather.path("icon").asText("");

                ForecastResponse forecast = new ForecastResponse(
                        cityName,
                        country,
                        dateTime,
                        main.path("temp").asDouble(0.0),
                        main.path("feels_like").asDouble(0.0),
                        main.path("humidity").asInt(0),
                        condition,
                        description,
                        wind.path("speed").asDouble(0.0),
                        icon
                );
                forecastList.add(forecast);
            }

            if (forecastList.isEmpty()) {
                throw new ExternalApiException("No forecast data available");
            }
            return forecastList;

        } catch (HttpClientErrorException.NotFound nf) {
            throw new CityNotFoundException("City not found: " + city);
        } catch (HttpClientErrorException hce) {
            throw new ExternalApiException("Weather provider error: " + hce.getStatusCode(), hce);
        } catch (RestClientException rce) {
            throw new ExternalApiException("Failed to call weather provider", rce);
        } catch (Exception e) {
            throw new ExternalApiException("Failed to parse forecast response", e);
        }
    }
}
