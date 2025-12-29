import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.css';

const Login = ({ setUser }) => {
  const [isRegisterMode, setIsRegisterMode] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [username, setUsername] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/users/login', {
        email,
        pwd: password,
      });

      const loggedInUser = {
        id: response.data.id,
        name: response.data.name,
        token: response.data.token,
      };

      localStorage.setItem('user', JSON.stringify(loggedInUser));
      setUser(loggedInUser);
      setError(null);
      navigate('/');
    } catch (err) {
      setError('Login failed. Please check your email and password.');
      console.error('Login error:', err);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    // Frontend validation
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    if (!email.includes('@') || !email.includes('.')) {
      setError('Please enter a valid email address.');
      return;
    }
    if (!username.trim()) {
      setError('Username is required.');
      return;
    }
    if (!firstName.trim()) {
      setError('First name is required.');
      return;
    }
    if (!lastName.trim()) {
      setError('Last name is required.');
      return;
    }
    if (!phoneNumber.trim()) {
      setError('Phone number is required.');
      return;
    }
    if (password.length < 6) {
      setError('Password must be at least 6 characters.');
      return;
    }

    try {
      const registerResponse = await axios.post('http://localhost:8080/users/register', {
        email,
        username,
        firstName,
        lastName,
        password,
        phoneNumber,
      });

      if (registerResponse.data === 'success') {
        // Registration successful, now auto-login
        const loginResponse = await axios.post('http://localhost:8080/users/login', {
          email,
          pwd: password,
        });

        const newUser = {
          id: loginResponse.data.id,
          name: loginResponse.data.name,
          token: loginResponse.data.token,
        };

        localStorage.setItem('user', JSON.stringify(newUser));
        setUser(newUser);
        setError(null);
        navigate('/');
      } else {
        setError(registerResponse.data);
      }
    } catch (err) {
      // Handle error response, ensuring it's a string
      const errorMessage = typeof err.response?.data === 'string' 
        ? err.response.data 
        : err.response?.data?.error || 'Registration failed. Please try again.';
      setError(errorMessage);
      console.error('Registration error:', err);
    }
  };

  const handleGuestLogin = () => {
    const guestUser = {
      id: -1,
      name: 'Guest',
      token: 'guest-token',
    };
    localStorage.setItem('user', JSON.stringify(guestUser));
    setUser(guestUser);
    navigate('/');
  };

  return (
    <div className="main-content">
      <h1 className="subtitle">{isRegisterMode ? 'Register for SpeakUp' : 'Login to SpeakUp'}</h1>
      
      {isRegisterMode ? (
        <form onSubmit={handleRegister} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="firstName">First Name:</label>
            <input
              type="text"
              id="firstName"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="lastName">Last Name:</label>
            <input
              type="text"
              id="lastName"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="phoneNumber">Phone Number:</label>
            <input
              type="text"
              id="phoneNumber"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password:</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="error">{error}</p>}
          <button type="submit" className="primary">Register</button>
        </form>
      ) : (
        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="error">{error}</p>}
          <button type="submit" className="primary">Login</button>
        </form>
      )}

      <p>
        {isRegisterMode ? (
          <span>
            Already have an account?{' '}
            <a href="#" onClick={(e) => { e.preventDefault(); setIsRegisterMode(false); }}>
              Login
            </a>
          </span>
        ) : (
          <span>
            Don't have an account?{' '}
            <a href="#" onClick={(e) => { e.preventDefault(); setIsRegisterMode(true); }}>
              Register
            </a>
          </span>
        )}
      </p>
      <p>
        Or <a href="#" onClick={(e) => { e.preventDefault(); handleGuestLogin(); }}>Browse as Guest</a>
      </p>
    </div>
  );
};

export default Login;