PROJECT TITLE:
Weather Application

ASSIGNMENT:
"Develop a weather application using a public API and display current weather conditions and forecasts."

PROJECT LOCATION:
C:\Users\maniv\OneDrive\Desktop\TechVedu-Project

CURRENT STACK:
- Frontend: React + Vite
- Backend: Java 17 + Spring Boot + Maven
- Database: MySQL 8.0
- API testing: Postman
- IDE: VS Code
- Git/GitHub
- Backend port: 8080
- MySQL database: techvedu_db

IMPORTANT:
The Spring Boot backend and MySQL connection are already working.
A temporary TestEntity was created only to verify JPA/MySQL.
Do NOT continue building the application around TestEntity.
Do NOT create unnecessary temporary entities.
The actual project must now be the Weather Application.

GOAL:
Build a clean, professional, full-stack weather application where the React frontend communicates with the Spring Boot backend, and the Spring Boot backend communicates with a public weather API.

ARCHITECTURE:

React Frontend
      ↓
Spring Boot REST API
      ↓
Public Weather API
      ↓
Weather Data
      ↓
React UI

Use the backend as the API layer instead of calling the public weather API directly from React.

DAY 7 TASKS:

1. ANALYZE THE EXISTING PROJECT
- Inspect the current TechVedu-Project structure.
- Inspect backend pom.xml.
- Inspect application.properties.
- Inspect existing Java packages.
- Inspect the existing frontend folder.
- Do not delete existing working files without a reason.

2. CLEAN TEMPORARY TEST CODE
- Identify TestEntity.java.
- Identify TestEntityRepository.java.
- Identify TestEntityService.java.
- Identify TestEntityController.java.
- Do NOT immediately delete them if doing so could break the current project.
- First verify whether they are only temporary test files.
- If safe, remove them because they are not part of the final Weather Application.
- Do not remove TestController.java until the new weather API is confirmed working.

3. BACKEND PACKAGE STRUCTURE

Create or maintain this structure:

backend/
└── src/
    └── main/
        └── java/
            └── com/
                └── techvedu/
                    └── backend/
                        ├── controller/
                        ├── service/
                        ├── dto/
                        ├── exception/
                        ├── config/
                        └── BackendApplication.java

4. WEATHER API DESIGN

Create a backend endpoint:

GET /api/weather?city=Chennai

The backend should:
- receive the city name
- validate that the city is not empty
- call a public weather API
- process the response
- return clean JSON to the frontend
- handle API errors properly

Do NOT expose the public weather API key to the React frontend.

5. PUBLIC WEATHER API

Use a reputable public weather API that provides:
- current weather
- temperature
- feels-like temperature
- humidity
- wind speed
- weather condition/description
- weather icon if available
- forecast data

Prefer OpenWeatherMap if a free API key is required.

Do not hard-code the API key directly into Java source code.

Use application.properties/environment configuration such as:

weather.api.key=${WEATHER_API_KEY}
weather.api.base-url=...

If an API key is required, create a clear configuration placeholder and explain exactly where I need to add my key.

Never commit a real API key to GitHub.

6. DTO

Create a clean WeatherResponse DTO containing useful fields such as:

- city
- country
- temperature
- feelsLike
- humidity
- windSpeed
- condition
- description
- icon

Also create a forecast DTO/model if required.

Do not expose unnecessary fields from the public API.

7. SERVICE

Create:

WeatherService.java

Responsibilities:
- receive city name
- call the public weather API
- parse the response
- convert it into our DTO
- handle API failures
- return clean application data

Use proper dependency injection.

8. CONTROLLER

Create:

WeatherController.java

Endpoint:

GET /api/weather?city=Chennai

Example response:

{
  "city": "Chennai",
  "country": "IN",
  "temperature": 30.5,
  "feelsLike": 34.2,
  "humidity": 70,
  "windSpeed": 4.2,
  "condition": "Clouds",
  "description": "scattered clouds",
  "icon": "03d"
}

Use appropriate HTTP status codes.

9. EXCEPTION HANDLING

Create a basic global exception handling mechanism.

Handle:
- empty city
- city not found
- weather API failure
- invalid API response
- server errors

Return clean JSON error responses.

10. CORS

Configure CORS so that the React Vite frontend can call:

http://localhost:5173

Do not use wildcard CORS if a specific frontend origin can be configured.

11. FRONTEND

Inspect the frontend folder.

If React + Vite is not initialized yet, initialize it.

Use:
- React
- Vite
- JavaScript
- Axios or fetch
- clean CSS / Tailwind if already configured

Do not introduce unnecessary frameworks.

12. FRONTEND UI

Create a professional weather dashboard containing:

HEADER:
- Weather application name
- current location/search area

SEARCH:
- city input
- Search button
- Enter key support

CURRENT WEATHER CARD:
- city
- country
- temperature
- weather condition
- description
- humidity
- wind speed
- feels-like temperature
- weather icon

FORECAST:
- upcoming forecast cards
- date
- temperature
- condition
- weather icon

STATES:
- loading state
- empty state
- city not found
- API error
- network error

RESPONSIVE DESIGN:
- desktop
- tablet
- mobile

13. FRONTEND API

The frontend must call:

http://localhost:8080/api/weather?city=Chennai

Do NOT call the public weather API directly from the frontend.

14. ENVIRONMENT VARIABLES

If required, use:

Frontend:
VITE_API_BASE_URL=http://localhost:8080

Backend:
WEATHER_API_KEY=${WEATHER_API_KEY}

Do not expose secrets.

15. DATABASE

Do NOT force weather data into MySQL yet.

For the basic assignment, weather information can be fetched live from the public API.

Keep the existing techvedu_db connection available for future features such as:
- search history
- favorite cities
- user accounts

Do not create unnecessary database tables on Day 7.

16. TESTING

After implementation:

Run:

.\mvnw.cmd clean test

Then run:

.\mvnw.cmd spring-boot:run

Test:

GET http://localhost:8080/api/weather?city=Chennai

Also test:
- another valid city
- invalid city
- empty city

Verify frontend can retrieve the backend response.

17. BUILD QUALITY

Use:
- clean package names
- constructor dependency injection
- DTOs
- service layer
- controller layer
- exception handling
- configuration properties
- readable code
- meaningful variable names
- no unnecessary dependencies

Do not use:
- hard-coded API keys
- System.out.println for application logging
- duplicated API logic
- unnecessary database tables
- unnecessary libraries

18. GIT

Do NOT commit automatically.

After completing the implementation, show me:

1. Files created
2. Files modified
3. Files deleted
4. Backend API endpoint
5. Frontend API configuration
6. Public API configuration
7. Commands used to run the backend
8. Commands used to run the frontend
9. Test results
10. Any manual steps I still need to perform

