import React, { useState } from "react";
import axios from "../../AxiosInterceptor";

function ContactPage() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (!name || !email || !phone || !message) {
            setError("Minden mező kitöltése kötelező!");
            return;
        }

        if (!validateEmail(email)) {
            setError("Kérjük, érvényes email címet adjon meg!");
            return;
        }

        try {
            await axios.post("/contact/submit", {
                name,
                email,
                phone,
                message,
            });
            alert("Üzenet sikeresen elküldve!");
            setName("");
            setEmail("");
            setPhone("");
            setMessage("");
        } catch (error) {
            alert("Hiba történt az üzenet küldésekor. Kérjük, próbálja újra később.");
        }
    };

    return (
        <div>
            <h1>Kapcsolat</h1>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Név</label>
                    <input
                        type="text"
                        id="name"
                        className="form-control"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="email" className="form-label">Email</label>
                    <input
                        type="email"
                        id="email"
                        className="form-control"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="phone" className="form-label">Telefonszám</label>
                    <input
                        type="tel"
                        id="phone"
                        className="form-control"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="message" className="form-label">Üzenet</label>
                    <textarea
                        id="message"
                        className="form-control"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        required
                    ></textarea>
                </div>
                <button type="submit" className="btn btn-primary">Küldés</button>
            </form>
        </div>
    );
}

export default ContactPage;
