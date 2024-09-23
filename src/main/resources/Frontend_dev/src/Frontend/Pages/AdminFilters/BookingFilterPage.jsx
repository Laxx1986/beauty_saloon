import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function BookingFilterPage() {
    const [bookings, setBooking] = useState([]);
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        axiosInstance.get("/bookings/all-booking") .then(response => {
            setBooking(response.data);

        })
            .catch(error => {
                console.error('Error fetching data:', error);
                if (error.response && error.response.status === 403) {
                    setFeedback('Hozzáférés megtagadva. Kérlek, jelentkezz be újra.');
                } else {
                    setFeedback('Hiba történt az adatok lekérésekor.');
                }
            });
    }, []);

    return (
        <>
            <div className="col-12">
                <h1 className="titleoflist">Foglalások listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Foglalás azonosító</th>
                                <th>Felhasználónév</th>
                                <th>Szolgáltatás</th>
                                <th>Dátum</th>
                                <th>Idő</th>
                                <th>Megjegyzés</th>
                            </tr>
                            </thead>
                            <tbody>
                            {bookings.map(booking => (
                                <tr className="rows" key={booking.bookingId}>
                                    <td>{booking.bookingId}</td>
                                    <td>{booking.username}</td>
                                    <td>{booking.service}</td>
                                    <td>{booking.date}</td>
                                    <td>{booking.time}</td>
                                    <td>{booking.comment}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="row">
                    <div className="col">
                        <Link to="/admin">
                            <button>Vissza</button>
                        </Link>
                    </div>
                </div>
            </div>
        </>
    );
}

export default BookingFilterPage;
