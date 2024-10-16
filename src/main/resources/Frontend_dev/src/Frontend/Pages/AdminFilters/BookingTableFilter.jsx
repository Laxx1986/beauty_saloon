import React, { useState, useEffect } from "react";
import axiosInstance from "../../../AxiosInterceptor";
import "./Table2.css";
import BookingEditModal from "./BookingEditModal";
import { Link } from "react-router-dom";

function BookingTableFilter() {
    const [bookings, setBookings] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedBookingId, setSelectedBookingId] = useState(null);
    const serviceProviderId = localStorage.getItem("serviceProviderId");

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = () => {
        // Check if the logged-in service provider is the one that should see all bookings
        const endpoint = serviceProviderId === '0d9b8997-21db-410a-ba83-02d132311073'
            ? '/bookings/all' // Endpoint for fetching all bookings
            : `/bookings/service-provider2/${serviceProviderId}`; // Endpoint for fetching bookings of the logged-in service provider

        axiosInstance
            .get(endpoint)
            .then((response) => {
                console.log("Fetched bookings:", response.data);
                const currentDate = new Date();

                // Filter out past bookings
                const upcomingBookings = response.data.filter((booking) => {
                    const bookingDate = new Date(booking.date + 'T' + booking.time);
                    return bookingDate >= currentDate;
                });

                setBookings(upcomingBookings);
            })
            .catch((error) => {
                console.error("Error fetching bookings:", error);
            });
    };

    const confirmBooking = (bookingId) => {
        const confirmMessage = "Biztosan meg szeretné erősíteni a foglalást?";
        if (window.confirm(confirmMessage)) {
            axiosInstance
                .put(`/bookings/confirm/${bookingId}`)
                .then(() => {
                    alert("Foglalás sikeresen megerősítve.");
                    fetchBookings();
                })
                .catch((error) => {
                    console.error("Error confirming booking:", error);
                });
        }
    };

    const unconfirmBooking = (bookingId) => {
        const confirmMessage = "Biztosan vissza szeretnéd vonni a megerősítést?";
        if (window.confirm(confirmMessage)) {
            axiosInstance
                .put(`/bookings/unconfirm/${bookingId}`)
                .then(() => {
                    alert("Foglalás megerősítése visszavonva.");
                    fetchBookings();
                })
                .catch((error) => {
                    console.error("Error unconfirming booking:", error);
                });
        }
    };

    const deleteBooking = (bookingId) => {
        const confirmMessage = "Biztosan törölni szeretné a foglalást?";
        if (window.confirm(confirmMessage)) {
            axiosInstance
                .delete(`/bookings/delete/${bookingId}`)
                .then(() => {
                    alert("Foglalás sikeresen törölve.");
                    fetchBookings();
                })
                .catch((error) => {
                    console.error("Error deleting booking:", error);
                });
        }
    };

    const openModal = (bookingId) => {
        setSelectedBookingId(bookingId);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedBookingId(null);
        fetchBookings();
    };

    return (
        <div>
            <div className="row">
                <div className="col">
                    <Link to="/admin">
                        <button>Vissza</button>
                    </Link>
                </div>
            </div>
            <h2>Hozzám foglaltak</h2>
            <table>
                <thead>
                <tr>
                    <th>Foglaló neve</th>
                    <th>Szolgáltatás Neve</th>
                    <th>Dátum</th>
                    <th>Időpont</th>
                    <th>Komment</th>
                    <th>Megerősítés</th>
                    <th>Módosítás</th>
                    <th>Törlés</th>
                </tr>
                </thead>
                <tbody>
                {bookings.map((booking) => (
                    <tr key={booking.bookingId}>
                        <td>{booking.name || 'Nincs adat'}</td>
                        <td>{booking.serviceName || 'Nincs adat'}</td>
                        <td>{booking.date}</td>
                        <td>{booking.time}</td>
                        <td>{booking.comment}</td>
                        <td>
                            {!booking.confirmed ? (
                                <button className="confirm-button" onClick={() => confirmBooking(booking.bookingId)}>
                                    Megerősít
                                </button>
                            ) : (
                                <button className="confirm-button" onClick={() => unconfirmBooking(booking.bookingId)}>
                                    Megerősítés visszavon
                                </button>
                            )}
                        </td>
                        <td>
                            <button className="edit-button" onClick={() => openModal(booking.bookingId)}>
                                Módosítás
                            </button>
                        </td>
                        <td>
                            <button className="delete-button" onClick={() => deleteBooking(booking.bookingId)}>
                                Törlés
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {isModalOpen && (
                <>
                    <div className="modal-overlay" />
                    <div className="modal">
                        <BookingEditModal
                            bookingId={selectedBookingId}
                            closeModal={closeModal}
                            serviceProviderId={serviceProviderId}
                            userId={localStorage.getItem("userId")}
                        />
                    </div>
                </>
            )}
        </div>
    );
}

export default BookingTableFilter;
