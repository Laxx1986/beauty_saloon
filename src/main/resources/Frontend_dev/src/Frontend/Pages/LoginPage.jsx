import React, { useState } from 'react';
import axiosInstance from '../.././AxiosInterceptor'; // Use your axiosInstance
import { useNavigate } from 'react-router-dom';

function LoginPage({ setIsLoggedIn, setUserName, setUserRights }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = (e) => {
        e.preventDefault();
        axiosInstance.post('/authenticate', { username, password })
            .then(response => {
                const token = response.data.jwt;
                const userRights = response.data.userRights;
                const userId = response.data.userId;

                localStorage.setItem('token', token);
                localStorage.setItem('isLoggedIn', 'true');
                localStorage.setItem('userName', username);
                localStorage.setItem('userRights', JSON.stringify(userRights));
                localStorage.setItem('userId', userId);
                setIsLoggedIn(true);
                setUserName(username);
                setUserRights(userRights);

                // Kérjük le a serviceProviderId-t az userId alapján
                return axiosInstance.get(`/serviceProviders/user/${userId}`);
            })
            .then(serviceProviderResponse => {
                if (serviceProviderResponse.status === 200) {
                    const serviceProviderId = serviceProviderResponse.data;
                    localStorage.setItem('serviceProviderId', serviceProviderId);
                    console.log("Service Provider ID: ", serviceProviderId);
                }
                navigate('/');
            })
            .catch(error => {
                // Ha nincs serviceProvider, folytassuk hibaüzenet nélkül
                if (error.response && error.response.status === 404) {
                    console.log("Ez a felhasználó nem szolgáltató.");
                    navigate('/');
                } else {
                    console.error('Login error:', error);
                    setError('Hibás felhasználónév vagy jelszó');
                }
            });
    };

    return (
        <div className="login-container">
            <form onSubmit={handleLogin}>
                <h2>Bejelentkezés</h2>
                {error && <p className="error">{error}</p>}
                <div>
                    <label>Felhasználónév:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Jelszó:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Bejelentkezés</button>
            </form>
        </div>
    );
}

export default LoginPage;
