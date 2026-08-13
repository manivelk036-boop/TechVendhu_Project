const WEATHER_ICON_BASE = 'https://openweathermap.org/img/wn/';

function ForecastSection({ forecast }) {
  if (!forecast || forecast.length === 0) {
    return null;
  }

  return (
    <section className="forecast-section">
      <div className="section-header">
        <h3>5-day forecast</h3>
      </div>

      <div className="forecast-grid">
        {forecast.map((item, index) => (
          <article key={`${item.dateTime}-${index}`} className="forecast-card">
            <div className="forecast-date">{item.dateTime}</div>
            {item.icon && (
              <img
                src={`${WEATHER_ICON_BASE}${item.icon}@2x.png`}
                alt={item.description || item.condition}
                className="forecast-icon"
              />
            )}
            <div className="forecast-main">{item.condition}</div>
            <div className="forecast-description">{item.description}</div>
            <div className="forecast-metrics">
              <span>{Math.round(item.temperature)}°C</span>
              <span>Feels {Math.round(item.feelsLike)}°C</span>
            </div>
            <div className="forecast-metrics small">
              <span>Humidity {item.humidity}%</span>
              <span>Wind {item.windSpeed} m/s</span>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}

export default ForecastSection;
