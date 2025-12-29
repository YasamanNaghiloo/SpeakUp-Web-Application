import React from 'react';
import './FilterSidebar.css';

const categories = [
  { label: 'Environmental', value: 'ENVIRONMENTAL' },
  { label: 'Political', value: 'POLITICAL' },
  { label: 'Social', value: 'SOCIAL' },
  { label: 'Local', value: 'LOCAL' },
  { label: 'Other', value: 'OTHER' },
];
const locations = ['Algeria', 'Austria', 'Azerbaijan', 'Bahrain', 'Brazil', 'Canada', 'Chad', 'Chile', 'China', 'Cuba', 'Denmark', 'Finland', 'France', 'Germany', 'Greece', 'Greenland', 'Iceland', 'India', 'Iran', 'Italy', 'Japan', 'Latvia', 'Mongolia', 'Morocco', 'Niger', 'Nigeria', 'Norway', 'Peru', 'Poland', 'Portugal', 'Romania', 'Russia', 'Slovakia', 'Somolia', 'South Korea', 'Spain', 'Sudan', 'Sweden', 'Switzerland', 'Taiwan', 'Thailand', 'Tunesia', 'Turkey', 'UK', 'Ukraine', 'USA', 'Yemen'];
export default function FilterSidebar({ isOpen, onClose, filters, setFilters }) {
  const toggleFilter = (key, value) => {
    const currentValues = filters[key] || [];
    const newValues = currentValues.includes(value)
      ? currentValues.filter(v => v !== value)
      : [...currentValues, value];
    setFilters(prev => ({ ...prev, [key]: newValues }));
  };

  return (
    <nav className={`filter-sidebar ${isOpen ? 'open' : ''}`} aria-hidden={!isOpen}>
      <button
        className="close-btn"
        onClick={onClose}
        aria-label="Close filter sidebar"
      >
        ✕
      </button>

      <div className="filter-section">
        <div className="header-filter">
          <h3>Filters</h3></div>
        <h3>Categories</h3>
        <div className="filter-options">
          {categories.map(cat => (
            <label key={cat.value}>
              <input
                type="checkbox"
                checked={filters.category?.includes(cat.value) || false}
                onChange={() => toggleFilter('category', cat.value)}
              />
              {cat.label}
            </label>
          ))}

        </div>
      </div>

      <div className="filter-section">
        <h3>Locations</h3>
        <div className="filter-options">
          {locations.map(loc => (
            <label key={loc}>
              <input
                type="checkbox"
                checked={filters.location?.includes(loc) || false}
                onChange={() => toggleFilter('location', loc)}
              />
              {loc}
            </label>
          ))}
        </div>
      </div>
    </nav>
  );
}
