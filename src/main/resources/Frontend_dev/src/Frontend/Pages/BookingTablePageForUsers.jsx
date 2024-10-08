import React, { useState, useEffect } from "react";
import axiosInstance from "../../AxiosInterceptor";
import "./AdminFilters/Table2.css";
import BookingEditModal from "./AdminFilters/BookingEditModal";
import { Link } from "react-router-dom";

function BookingTablePageForUsers() {
    const [bookings, setBookings] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedBookingId, setSelectedBookingId] = useState(null);
    const userId = localStorage.getItem("userId");

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = () => {
        axiosInstance
            .get(`/bookings/user/${userId}`)
            .then((response) => {
                console.log("Fetched bookings:", response.data);
                const currentDate = new Date();

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
            <h2>Foglalásaim</h2>
            <table>
                <thead>
                <tr>
                    <th>Foglaló neve</th>
                    <th>Szolgáltatás Neve</th>
                    <th>Dátum</th>
                    <th>Időpont</th>
                    <th>Komment</th>
                    <th>Állapot</th>
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
                            {booking.confirmed ? 'Foglalás megerősítve' : 'Foglalás nincs megerősítve'}
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
                            serviceProviderId={localStorage.getItem("serviceProviderId")}
                            userId={userId}
                        />
                    </div>
                </>
            )}
        </div>
    );
}

export default BookingTablePageForUsers;
