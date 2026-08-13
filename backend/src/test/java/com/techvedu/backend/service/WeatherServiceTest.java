package com.techvedu.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techvedu.backend.dto.ForecastResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WeatherServiceTest {

    @Test
    void getForecastByCity_returnsFiveDayForecast() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        WeatherService service = new WeatherService(restTemplate, new ObjectMapper(),
                "https://api.openweathermap.org/data/2.5", "test-api-key");

        String payload = "{"
                + "\"city\":{\"name\":\"Chennai\",\"country\":\"IN\"},"
                + "\"list\":["
                + "{\"dt\":1700000000,\"main\":{\"temp\":30.5,\"feels_like\":31.1,\"humidity\":65},\"weather\":[{\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"wind\":{\"speed\":4.2}},"
                + "{\"dt\":1700086400,\"main\":{\"temp\":29.8,\"feels_like\":30.7,\"humidity\":68},\"weather\":[{\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"wind\":{\"speed\":5.1}},"
                + "{\"dt\":1700172800,\"main\":{\"temp\":31.1,\"feels_like\":32.0,\"humidity\":61},\"weather\":[{\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"wind\":{\"speed\":3.8}},"
                + "{\"dt\":1700259200,\"main\":{\"temp\":28.9,\"feels_like\":29.6,\"humidity\":74},\"weather\":[{\"main\":\"Drizzle\",\"description\":\"moderate rain\",\"icon\":\"09d\"}],\"wind\":{\"speed\":6.2}},"
                + "{\"dt\":1700345600,\"main\":{\"temp\":27.6,\"feels_like\":28.3,\"humidity\":78},\"weather\":[{\"main\":\"Rain\",\"description\":\"heavy rain\",\"icon\":\"10d\"}],\"wind\":{\"speed\":7.5}}"
                + "]}"
                ;

        server.expect(requestTo("https://api.openweathermap.org/data/2.5/forecast?q=Chennai&appid=test-api-key&units=metric"))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        List<ForecastResponse> forecast = service.getForecastByCity("Chennai");

        assertEquals(5, forecast.size());
        assertEquals("Chennai", forecast.get(0).getCity());
        assertEquals("IN", forecast.get(0).getCountry());
        assertEquals("Clouds", forecast.get(0).getCondition());
        assertEquals("scattered clouds", forecast.get(0).getDescription());
        assertEquals("03d", forecast.get(0).getIcon());
        assertEquals(30.5, forecast.get(0).getTemperature(), 0.01);
        assertEquals(31.1, forecast.get(0).getFeelsLike(), 0.01);
        assertEquals(65, forecast.get(0).getHumidity());
        assertEquals(4.2, forecast.get(0).getWindSpeed(), 0.01);

        server.verify();
    }
}
