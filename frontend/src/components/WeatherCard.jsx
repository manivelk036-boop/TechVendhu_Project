const WEATHER_ICON_BASE = 'https://openweathermap.org/img/wn/';

function WeatherInfoRow({ label, value }) {
  return (
    <div className="info-row">
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

function WeatherCard({ weather }) {
  if (!weather) {
    return null;
  }

  const iconUrl = weather.icon ? `${WEATHER_ICON_BASE}${weather.icon}@2x.png` : null;

  return (
    <section className="weather-card">
      <div className="card-header">
        <div>
          <p className="eyebrow">Current weather</p>
          <h2>
            {weather.city}, {weather.country}
          </h2>
        </div>
        {iconUrl && <img src={iconUrl} alt={weather.description || weather.condition} className="weather-icon" />}
      </div>

      <div className="current-temp-row">
        <div className="temperature">{Math.round(weather.temperature)}°C</div>
        <div className="condition-block">
          <p>{weather.condition}</p>
          <span>{weather.description}</span>
        </div>
      </div>

      <div className="weather-grid">
        <WeatherInfoRow label="Feels like" value={`${Math.round(weather.feelsLike)}°C`} />
        <WeatherInfoRow label="Humidity" value={`${weather.humidity}%`} />
        <WeatherInfoRow label="Wind speed" value={`${weather.windSpeed} m/s`} />
        <WeatherInfoRow label="Condition" value={weather.condition} />
      </div>
    </section>
  );
}

export default WeatherCard;
