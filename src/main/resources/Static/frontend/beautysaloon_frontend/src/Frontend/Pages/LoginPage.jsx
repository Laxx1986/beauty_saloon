import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginPage({ setLogin, setUserName, setUserRights }) {
    const [localUserName, setLocalUserName] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        const userName = localStorage.getItem('userName');
        const userRightsString = localStorage.getItem('userRights');
        const userId = localStorage.getItem('userId');

        let userRights = null;
        try {
            if (userRightsString) {
                userRights = JSON.parse(userRightsString); // Parse JSON safely
            }
        } catch (error) {
            console.error("Error parsing userRights from localStorage:", error);
        }

        if (isLoggedIn === 'true' && userName && userId) {
            setLogin(true);
            setUserName(userName);
            setUserRights(userRights);
            navigate('/');
        }
    }, [setLogin, setUserName, setUserRights, navigate]);


    const handleLogin = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/users/login', {
                userName: localUserName,
                password
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
                withCredentials: true
            });

            setLogin(true);
            setUserName(response.data.userName);
            setUserRights(response.data.userRights);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userName', response.data.userName);
            localStorage.setItem('userRights', JSON.stringify(response.data.userRights)); // Ensure valid JSON
            localStorage.setItem('userId', response.data.userId);

            navigate('/');
        } catch (error) {
            console.error("Login failed", error);
        }
    };


    return (
        <div>
            <h2>Login</h2>
            <input
                type="text"
                placeholder="Username"
                value={localUserName}
                onChange={(e) => setLocalUserName(e.target.value)}
                required
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            <button onClick={handleLogin} disabled={isLoading}>
                {isLoading ? 'Logging in...' : 'Login'}
            </button>

            {errorMessage && (
                <p style={{ color: 'red' }}>{errorMessage}</p>
            )}

            {/* For debugging purposes, you can see stored data in the console */}
            {process.env.NODE_ENV === 'development' && (
                <>
                    <p>User Rights (Stored): {localStorage.getItem('userRights')}</p>
                    <p>User Name (Stored): {localStorage.getItem('userName')}</p>
                    <p>User ID (Stored): {localStorage.getItem('userId')}</p>
                </>
            )}
        </div>
    );
}

export default LoginPage;
