package com.techvedu.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techvedu.backend.dto.WeatherResponse;
import com.techvedu.backend.exception.CityNotFoundException;
import com.techvedu.backend.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
}
