import React, { useState } from 'react';
import axiosInstance from '../../AxiosInterceptor';

function RegistrationPage() {
    const [formData, setFormData] = useState({
        name: '',
        userName: '',
        email: '',
        phoneNumber: '',
        password: '',
        repassword: ''
    });

    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setSuccessMessage('');  // Üzenetek törlése a submit elején
        setErrorMessage('');

        if (formData.password !== formData.repassword) {
            setErrorMessage('A jelszavak nem egyeznek');
            return;
        }

        try {
            const response = await axiosInstance.post('/users/register', formData);
            setSuccessMessage("Sikeres regisztráció");
            setFormData({
                name: '',
                userName: '',
                email: '',
                phoneNumber: '',
                password: '',
                repassword: ''
            }); // Űrlap ürítése
        } catch (error) {
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message); // A backend hibaüzenetének megjelenítése
            } else {
                setErrorMessage('Sikertelen regisztráció. Kérjük, próbálja újra!');
            }
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-sm-10 col-md-8 col-lg-6">
                    <form onSubmit={handleSubmit} method="POST">
                        {/* Hibaüzenet megjelenítése */}
                        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

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
                    {/* Sikeres regisztráció üzenet megjelenítése */}
                    {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
                </div>
            </div>
        </div>
    );
}

export default RegistrationPage;
