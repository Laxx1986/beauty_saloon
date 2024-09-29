import React, { useState, useEffect } from "react";
import axiosInstance from "../../../AxiosInterceptor";
import "./Table2.css";
import BookingEditModal from "./BookingEditModal"; // Import the BookingEditModal

function BookingTableFilter() {
    const [bookings, setBookings] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedBookingId, setSelectedBookingId] = useState(null);
    const serviceProviderId = localStorage.getItem("serviceProviderId");

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = () => {
        axiosInstance
            .get(`/bookings/service-provider2/${serviceProviderId}`)
            .then((response) => {
                console.log("Fetched bookings:", response.data);
                const currentDate = new Date();

                // Szűrjük ki a múltbeli foglalásokat
                const upcomingBookings = response.data.filter((booking) => {
                    const bookingDate = new Date(booking.date + 'T' + booking.time); // Kombináljuk a dátumot és az időt
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
        console.log(bookingId);
        console.log(selectedBookingId);
        console.log(isModalOpen);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedBookingId(null);
        fetchBookings(); // Fetch updated bookings after closing the modal
    };

    return (
        <div>
            <h2>Hozzám foglaltak</h2>
            <table>
                <thead>
                <tr>
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
                    <div className="modal-overlay"/>
                    <div className="modal"> {/* Modal ablak */}
                        <BookingEditModal
                            bookingId={selectedBookingId}
                            closeModal={closeModal}
                        />
                    </div>
                </>
            )}
        </div>
    );
}

export default BookingTableFilter;
