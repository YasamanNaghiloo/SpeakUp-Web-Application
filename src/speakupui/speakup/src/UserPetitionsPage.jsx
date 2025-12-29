import React, { useState } from 'react';
import './App.css';
import { Link } from 'react-router-dom';
import PetitionsList from './PetitionsList';

const UserPetitionsPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [filter] = useState('all');
  const [sort, setSort] = useState('newest');
  const [searchTriggered, setSearchTriggered] = useState(false);

  const user = JSON.parse(localStorage.getItem('user'));
  const userId = user ? user.id : null;

  const handleSearch = () => {
    setSearchTriggered(prev => !prev);
  };

  if (!userId) {
    return <div>Please log in to view your petitions.</div>;
  }

  return (
    <div className="App">
      <header className="top-header">
        <h1 className="app-title">My Petitions</h1>

        <div className="header-search-menu">
          <div className="header-search">
            <input
              type="text"
              placeholder="Search my petitions..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button onClick={handleSearch} className="search-btn">🔍</button>
          </div>

          <div className="horizantal-menu">
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/create">Create Petition</Link></li>
              <li><Link to="/login">Logout</Link></li>
            </ul>
          </div>
        </div>
      </header>

      <img src="/banner.jpg" alt="SpeakUp Banner" className="banner" />

      <main className="main-content">
        <p className="subtitle">Your Petitions</p>

        <div className="sort-toggle">
          <span>Sort By </span>
          <button
            className={sort === 'newest' ? 'sort-btn active' : 'sort-btn'}
            onClick={() => {
              setSort('newest');
              setSearchTriggered(prev => !prev);
            }}
          >
            Newest
          </button>
          <button
            className={sort === 'most-signed' ? 'sort-btn active' : 'sort-btn'}
            onClick={() => {
              setSort('most-signed');
              setSearchTriggered(prev => !prev);
            }}
          >
            Most Signed
          </button>
        </div>

        <PetitionsList
          sort={sort}
          searchQuery={searchQuery}
          filter={filter}
          searchTrigger={searchTriggered}
          userId={userId}
          isUserPetitions={true}
        />
      </main>
    </div>
  );
};

export default UserPetitionsPage;