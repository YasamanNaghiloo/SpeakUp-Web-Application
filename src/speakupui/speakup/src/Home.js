import React, { useState, useEffect } from 'react';
import PetitionsList from './PetitionsList';
import FilterSidebar from './FilterSidebar';
import './App.css';
import { Link, useNavigate } from 'react-router-dom';

function Home({ user, logout }) {
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState({
  category: [],
  location: [],
  locationScope: "COUNTRY",
});

  const [sort, setSort] = useState('newest');
  const [signFilter, setSignFilter] = useState('all');
  const [searchTriggered, setSearchTriggered] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setSearchTriggered(prev => !prev);
  }, [filters, sort, signFilter, searchQuery]);

  const handleAuthAction = () => {
    if (user && user.id === -1) {
      navigate('/login');
    } else {
      logout();
    }
  };

  return (
    <div className="App">
      <header className="top-header">
        <h1 className="app-title">SpeakUp!</h1>
        {user && <div className="welcome-text">Welcome, {user.name}</div>}

        <div className="header-search-menu">
          <div className="header-search">
            <input
              type="text"
              placeholder="Search petitions or users..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{ width: '200px' }}
            />
          </div>

          <button onClick={() => setSearchTriggered(prev => !prev)} className="search-btn">
            🔍
          </button>
          <button
            onClick={() => setSidebarOpen(true)}
            className="filters-btn"
            aria-label="Open filter menu"
          >
            ☰
          </button>

          <div className="horizantal-menu" style={{ display: 'inline-block', marginLeft: '20px' }}>
            <ul>
              <li><Link to="/user-petitions">My Petitions</Link></li>
              <li><Link to="/create">Create Petition</Link></li>
              <li>
                <span onClick={handleAuthAction} className="menu-link" style={{ cursor: 'pointer' }}>
                  {user && user.id === -1 ? 'Login' : 'Logout'}
                </span>
              </li>
            </ul>
          </div>
        </div>
      </header>
      {sidebarOpen && (
        <div
          className="filter-overlay active"
          onClick={() => setSidebarOpen(false)}
          aria-hidden="true"
        />
      )}

      <FilterSidebar
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        filters={filters}
        setFilters={setFilters}
        signFilter={signFilter}
        setSignFilter={setSignFilter}
      />

      <img src="/banner.jpg" alt="SpeakUp Banner" className="banner" />

      <main className="main-content">
        <p className="subtitle">Raise your voice. Support causes. Make change.</p>

        <div className="sort-toggle">
          <span>Sort By </span>
          <button
            className={sort === 'newest' ? 'sort-btn active' : 'sort-btn'}
            onClick={() => setSort('newest')}
          >
            Newest
          </button>
          <button
            className={sort === 'most-signed' ? 'sort-btn active' : 'sort-btn'}
            onClick={() => setSort('most-signed')}
          >
            Most Signed
          </button>
        </div>

        <PetitionsList
          sort={sort}
          searchQuery={searchQuery}
          filter={filters}
          searchTrigger={searchTriggered}
          userId={user?.id}
          isUserPetitions={false}
          signFilter={signFilter}
        />
      </main>
    </div>
  );
}

export default Home;