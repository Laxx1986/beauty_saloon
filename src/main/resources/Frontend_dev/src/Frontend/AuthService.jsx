import axios from 'axios';

const API_URL = 'http://localhost:8080/api/authenticate';

export const authenticateUser = async (username, password) => {
    try {
        const response = await axios.post(API_URL, { username, password });
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userName', username);
            localStorage.setItem('userRights', JSON.stringify(userRights));
        }
        return response.data;
    } catch (error) {
        console.error("Authentication failed", error);
        throw error;
    }
};

export const logoutUser = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('userName');
    localStorage.removeItem('userRights');
};
