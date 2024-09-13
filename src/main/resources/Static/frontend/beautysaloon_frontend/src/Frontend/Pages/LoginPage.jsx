import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginPage({ setLogin, setUserName, setUserRights }) {
    const [localUserName, setLocalUserName] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        const userName = localStorage.getItem('userName');
        const userRights = JSON.parse(localStorage.getItem('userRights')); // Parse the JSON string
        const userId = localStorage.getItem('userId');

        if (isLoggedIn === 'true' && userName && userId) {
            setLogin(true);
            setUserName(userName);
            setUserRights(userRights); // Pass the entire userRights object
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
                }
            });

            setLogin(true);
            setUserName(response.data.userName);
            setUserRights(response.data.userRights); // Set the entire userRights object
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userName', response.data.userName);
            localStorage.setItem('userRights', JSON.stringify(response.data.userRights)); // Store as JSON string
            localStorage.setItem('userId', response.data.userId);


            navigate('/');
        } catch (error) {
            console.error("Login failed", error);
        }
        console.log(localStorage.getItem('userRights'));
        console.log(localStorage.getItem('userName'));
        console.log(localStorage.getItem('userId'));
    };

    return (
        <div>
            <h2>Login</h2>
            <input
                type="text"
                placeholder="Username"
                value={localUserName}
                onChange={(e) => setLocalUserName(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default LoginPage;

