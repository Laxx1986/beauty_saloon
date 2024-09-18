import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';

function UserModifyPage() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [user, setUser] = useState({
        email: '',
        phoneNumber: '',
        password: ''
    });

    useEffect(() => {
        axios.get(`http://localhost:8080/api/users/${userId}`, {
            auth: {
                username: 'admin',
                password: 'almafa'
            }
        })
            .then(response => {
                setUser(response.data);
            })
            .catch(error => {
                console.error('Error fetching user data:', error);
            });
    }, [userId]);

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setUser({ ...user, [name]: value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        axios.put(`http://localhost:8080/api/users/${userId}`, user, {
            auth: {
                username: 'admin',
                password: 'almafa'
            }
        })
            .then(response => {
                navigate('/userfilter');
            })
            .catch(error => {
                console.error('Error updating user:', error);
            });
    };

    return (
        <div className="container">
            <h1>Felhasználó módosítása</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Felhasználói név</label>
                    <input type="text" className="form-control" value={user.userName} disabled />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" className="form-control" name="email" value={user.email} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Telefonszám</label>
                    <input type="text" className="form-control" name="phoneNumber" value={user.phoneNumber} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Jelszó</label>
                    <input type="password" className="form-control" name="password" value={user.password} onChange={handleInputChange} />
                </div>
                <button type="submit" className="btn btn-primary">Mentés</button>
            </form>
        </div>
    );
}

export default UserModifyPage;
