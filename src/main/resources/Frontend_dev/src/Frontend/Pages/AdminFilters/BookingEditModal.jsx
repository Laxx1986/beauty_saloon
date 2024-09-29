import React, { useState, useEffect } from "react";
import axiosInstance from "../../../AxiosInterceptor";

function BookingEditModal({ bookingId, closeModal }) {
    const [booking, setBooking] = useState({
        date: "",
        time: "",
        comment: "",
        serviceId: ""
    });
    const [services, setServices] = useState([]);

    useEffect(() => {
        fetchBooking();
    }, [bookingId]);

    const fetchBooking = () => {
        axiosInstance
            .get(`/bookings/${bookingId}`)
            .then((response) => {
                setBooking(response.data);
                fetchServices(response.data.serviceProviderID);
            })
            .catch((error) => {
                console.error("Error fetching booking:", error);
            });
    };

    const fetchServices = (serviceProviderId) => {
        axiosInstance
            .get(`/bookings/service-provider/${serviceProviderId}`)
            .then((response) => {
                setServices(response.data);
            })
            .catch((error) => {
                console.error("Error fetching services:", error);
            });
    };

    const handleChange = (e) => {
        let { name, value } = e.target;

        // Ha az input mező 'time' típusú, biztosítjuk, hogy csak az "HH:mm" formátumot használjuk
        if (name === "time" && value.length > 5) {
            value = value.substring(0, 5); // Levágjuk a másodperceket, csak "HH:mm" marad
        }
        console.log(`Módosított mező: ${name}, Érték: ${value}`);
        setBooking({
            ...booking,
            [name]: value
        });
    };


    const handleSubmit = (e) => {
        e.preventDefault();
        axiosInstance
            .put(`/bookings/update/${bookingId}`, booking)
            .then(() => {
                alert("Foglalás sikeresen módosítva.");
                closeModal(); // Close the modal after updating
            })
            .catch((error) => {
                console.error("Error updating booking:", error);
            });
    };

    return (
        <div className="modal">
            <h2>Foglalás Módosítása</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Szolgáltatás:
                    <select
                        name="serviceId"
                        value={booking.serviceId}
                        onChange={handleChange}
                    >
                        <option value="">Válassz szolgáltatást</option>
                        {services.map((service) => (
                            <option key={service.serviceId} value={service.serviceId}>
                                {service.serviceName}
                            </option>
                        ))}
                    </select>
                </label>
                <br />
                <label>
                    Dátum:
                    <input
                        type="date"
                        name="date"
                        value={booking.date}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <label>
                    Időpont:
                    <input
                        type="time"
                        name="time"
                        value={booking.time}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <label>
                    Komment:
                    <input
                        type="text"
                        name="comment"
                        value={booking.comment}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <button type="submit">Módosítás</button>
                <button type="button" onClick={closeModal}>Mégsem</button>
            </form>
        </div>
    );
}

export default BookingEditModal;
