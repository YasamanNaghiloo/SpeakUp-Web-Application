import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import Home from './Home';
import PetitionDetailView from './PetitionDetails';
import Login from './Login';
import CreatePetition from './CreatePetition';
import UserPetitionsPage from './UserPetitionsPage';

const App = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const logout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };

  const ProtectedRoute = ({ children }) => {
    if (!user || (user.id === -1)) {
      return <Navigate to="/login" replace />;
    }
    return children;
  };

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route
          path="/"
          element={
            user ? (
              <>
                <Home user={user} logout={logout} />
              </>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />

        <Route
          path="/petition/:id"
          element={
            user ? (
              <>
                {user.id === -1 && <div className="guest-notice">Browsing as Guest - Limited Access</div>}
                <PetitionDetailView user={user} />
              </>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
        <Route
          path="/create"
          element={
            <ProtectedRoute>
              <CreatePetition />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user-petitions"
          element={
            <ProtectedRoute>
              <UserPetitionsPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </Router>
  );
};

export default App;