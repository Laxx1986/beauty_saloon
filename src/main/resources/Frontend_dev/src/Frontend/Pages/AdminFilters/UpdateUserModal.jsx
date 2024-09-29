import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import axiosInstance from '../../../AxiosInterceptor';
import './Tables.css';

Modal.setAppElement('#root'); // Állítsuk be a root elemet

function UpdateUserModal({ isOpen, onClose, userId, userName, initialData, refreshUsers }) {
    const [name, setName] = useState(initialData.name || '');
    const [email, setEmail] = useState(initialData.email || '');
    const [phoneNumber, setPhoneNumber] = useState(initialData.phoneNumber || '');
    const [password, setPassword] = useState('');
    const [rePassword, setRePassword] = useState('');
    const [feedback, setFeedback] = useState('');

    const handleUpdate = () => {
        if (password !== rePassword) {
            setFeedback('A megadott jelszavak nem egyeznek.');
            return;
        }

        const updatedUser = {
            name,
            email,
            phoneNumber,
            password: password || undefined, // Csak akkor küldjük el, ha meg van adva
        };

        axiosInstance.put(`/users/update/${userId}`, updatedUser)
            .then(() => {
                setFeedback('A felhasználó adatai frissítve lettek.');
                refreshUsers(); // Frissítjük a felhasználói listát
                onClose(); // Zárjuk be a modális ablakot
            })
            .catch(error => {
                console.error('Error updating user:', error);
                setFeedback('Hiba történt a felhasználó frissítésekor.');
            });
    };

    return (
        <Modal isOpen={isOpen} onRequestClose={onClose} contentLabel="Update User">
            <h2>Felhasználó adatainak frissítése</h2>
            {feedback && <p className="feedback">{feedback}</p>}
            <form>
                <div>
                    <label>Felhasználónév (nem módosítható):</label>
                    <input type="text" value={userName} disabled />
                </div>
                <div>
                    <label>Név:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div>
                    <label>Telefonszám:</label>
                    <input
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                </div>
                <div>
                    <label>Jelszó:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <label>Jelszó megerősítése:</label>
                    <input
                        type="password"
                        value={rePassword}
                        onChange={(e) => setRePassword(e.target.value)}
                    />
                </div>
                <button type="button" onClick={handleUpdate}>Frissítés</button>
                <button type="button" onClick={onClose}>Mégse</button>
            </form>
        </Modal>
    );
}

export default UpdateUserModal;
