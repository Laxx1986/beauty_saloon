import React, { useState } from 'react';
import axios from 'axios';

function RegistrationPage() {
    const [formData, setFormData] = useState({
        name: '',
        username: '',
        email: '',
        phone: '',
        password: '',
        repassword: ''
    });

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        console.log('FormData:', formData);
        try {
            const response = await axios.post('http://localhost:8080/api/users/register', formData, {
                auth: {
                    username: 'admin', // Felhasználónév
                    password: 'almafa' // Jelszó
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-sm-10 col-md-8 col-lg-6">
                    <form onSubmit={handleSubmit} method="POST">
                        <label>
                            Név: <br />
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Felhasználónév: <br />
                            <input
                                type="text"
                                name="userName"
                                value={formData.userName}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Email: <br />
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Telefonszám: <br />
                            <input
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Jelszó: <br />
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Jelszó újra: <br />
                            <input
                                type="password"
                                name="repassword"
                                value={formData.repassword}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <button type="submit">Regisztráció</button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default RegistrationPage;
