function SearchForm({ value, onChange, onSubmit, loading }) {
  return (
    <form className="search-form" onSubmit={onSubmit}>
      <label className="search-label" htmlFor="city-search">
        City
      </label>
      <div className="search-row">
        <input
          id="city-search"
          type="text"
          value={value}
          onChange={(event) => onChange(event.target.value)}
          placeholder="Enter a city name"
          aria-label="Search city"
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Loading...' : 'Search'}
        </button>
      </div>
    </form>
  );
}

export default SearchForm;
