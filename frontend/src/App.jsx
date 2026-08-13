import { useState } from 'react';
import './App.css';
import SearchForm from './components/SearchForm';
import WeatherCard from './components/WeatherCard';
import ForecastSection from './components/ForecastSection';

const BASE_URL = 'http://localhost:8080/api/weather';

function App() {
  const [city, setCity] = useState('Chennai');
  const [weather, setWeather] = useState(null);
  const [forecast, setForecast] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchWeather = async (cityName) => {
    const trimmedCity = cityName.trim();

    if (!trimmedCity) {
      setWeather(null);
      setForecast([]);
      setError('Please enter a valid city name.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const weatherResponse = await fetch(`${BASE_URL}?city=${encodeURIComponent(trimmedCity)}`);
      if (!weatherResponse.ok) {
        const errorPayload = await weatherResponse.json().catch(() => ({}));
        throw new Error(errorPayload.message || 'Unable to fetch weather data.');
      }

      const weatherData = await weatherResponse.json();
      setWeather(weatherData);

      const forecastResponse = await fetch(`${BASE_URL}/forecast?city=${encodeURIComponent(trimmedCity)}`);
      if (!forecastResponse.ok) {
        const errorPayload = await forecastResponse.json().catch(() => ({}));
        if (forecastResponse.status === 404) {
          throw new Error(errorPayload.message || 'City not found.');
        }
        throw new Error(errorPayload.message || 'Unable to fetch forecast data.');
      }

      const forecastData = await forecastResponse.json();
      setForecast(Array.isArray(forecastData) ? forecastData : []);
    } catch (fetchError) {
      setWeather(null);
      setForecast([]);
      setError(fetchError.message || 'Something went wrong. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await fetchWeather(city);
  };

  return (
    <main className="app-shell">
      <div className="weather-dashboard">
        <header className="topbar">
          <div>
            <p className="eyebrow">TechVedu</p>
            <h1>Weather Dashboard</h1>
          </div>
        </header>

        <SearchForm value={city} onChange={setCity} onSubmit={handleSubmit} loading={loading} />

        {error && <div className="status-message error">{error}</div>}

        {!loading && !error && !weather && (
          <div className="status-message neutral">No weather data available yet. Search for a city to begin.</div>
        )}

        {loading && <div className="status-message neutral">Loading weather data...</div>}

        {weather && <WeatherCard weather={weather} />}
        {!loading && !error && forecast.length === 0 && weather && (
          <div className="status-message neutral">No forecast data available for this city.</div>
        )}
        {forecast.length > 0 && <ForecastSection forecast={forecast.slice(0, 5)} />}
      </div>
    </main>
  );
}

export default App;
