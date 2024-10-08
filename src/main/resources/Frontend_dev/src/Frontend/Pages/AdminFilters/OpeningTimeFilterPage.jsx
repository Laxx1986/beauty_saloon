import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function OpeningTimeFilterPage() {
    const [openingTimes, setOpeningTimes] = useState([]);
    const [feedback, setFeedback] = useState('');
    const [currentMonth, setCurrentMonth] = useState(new Date().getMonth());
    const [currentYear, setCurrentYear] = useState(new Date().getFullYear());
    const [newOpeningTime, setNewOpeningTime] = useState({
        year: '',
        month: '',
        timeFrom: '',
        timeTo: '',
        selectedDays: []
    });
    const loggedInServiceProviderId = localStorage.getItem('serviceProviderId');
    const [bookedDates, setBookedDates] = useState([]);

    useEffect(() => {
        fetchOpeningTimes();
        fetchBookedDates();
    }, [currentMonth, currentYear]);

    const fetchOpeningTimes = () => {
        axiosInstance
            .get(`/openingTimes/${loggedInServiceProviderId}/opening-times`)
            .then((response) => {
                const filteredTimes = response.data
                    .filter((ot) => {
                        const date = new Date(ot.date);
                        return (
                            date.getMonth() === currentMonth &&
                            date.getFullYear() === currentYear
                        );
                    })
                    .sort((a, b) => new Date(a.date) - new Date(b.date)); // Rendezés dátum szerint
                setOpeningTimes(filteredTimes);
            })
            .catch((error) => {
                console.error("Hiba az adatok lekérésekor:", error);
                if (error.response && error.response.status === 403) {
                    setFeedback("Hozzáférés megtagadva. Kérlek, jelentkezz be újra.");
                } else {
                    setFeedback("Hiba történt az adatok lekérésekor.");
                }
            });
    };


    const fetchBookedDates = () => {
        axiosInstance
            .get(`/bookings/service-provider/${loggedInServiceProviderId}/booked-dates`)
            .then((response) => {
                setBookedDates(response.data);
                console.log(response.data);
            })
            .catch((error) => {
                console.error("Hiba a foglalt dátumok lekérésekor:", error);
            });
    };

    const handleDeleteOpeningTime = (openingTimeId) => {
        axiosInstance
            .delete(`/openingTimes/delete/${openingTimeId}`)
            .then(() => {
                setFeedback("Nyitvatartási idő sikeresen törölve.");
                fetchOpeningTimes(); // Lista frissítése
            })
            .catch((error) => {
                console.error("Hiba a nyitvatartási idő törlésekor:", error);
                setFeedback("Hiba történt a nyitvatartási idő törlésekor.");
            });
    };

    const handlePreviousMonth = () => {
        if (currentMonth === 0) {
            setCurrentMonth(11);
            setCurrentYear(currentYear - 1);
        } else {
            setCurrentMonth(currentMonth - 1);
        }
    };

    const handleNextMonth = () => {
        if (currentMonth === 11) {
            setCurrentMonth(0);
            setCurrentYear(currentYear + 1);
        } else {
            setCurrentMonth(currentMonth + 1);
        }
    };

    const isDateBooked = (date) => {
        const formattedDate = new Date(date).toISOString().split('T')[0];
        return bookedDates.includes(formattedDate);
    };

    const getAvailableMonths = () => {
        const months = [...Array(12).keys()];
        if (newOpeningTime.year === currentYear.toString()) {
            // Szűrje ki az elmúlt hónapokat az aktuális évből
            return months.filter(month => month >= currentMonth);
        }
        return months;
    };

    const getDaysInMonth = (month, year) => {
        const date = new Date(year, month, 1);
        const days = [];
        while (date.getMonth() === month) {
            const day = date.getDate();
            if (new Date(year, month, day) >= new Date()) {
                days.push(day);
            }
            date.setDate(date.getDate() + 1);
        }
        return days;
    };

    const handleNewOpeningTimeChange = (e) => {
        setNewOpeningTime({
            ...newOpeningTime,
            [e.target.name]: e.target.value,
        });
    };

    const handleDaySelection = (day) => {
        setNewOpeningTime((prev) => {
            const selectedDays = [...prev.selectedDays];
            if (selectedDays.includes(day)) {
                return {
                    ...prev,
                    selectedDays: selectedDays.filter((d) => d !== day),
                };
            } else {
                return {
                    ...prev,
                    selectedDays: [...selectedDays, day],
                };
            }
        });
    };

    const handleAddOpeningTimes = (e) => {
        e.preventDefault();

        if (!newOpeningTime.timeFrom || !newOpeningTime.timeTo || newOpeningTime.selectedDays.length === 0) {
            setFeedback("Kérjük, töltsd ki az összes mezőt és válassz ki legalább egy napot.");
            return;
        }

        const requestBody = {
            serviceProviderId: loggedInServiceProviderId,
            timeFrom: newOpeningTime.timeFrom,
            timeTo: newOpeningTime.timeTo,
            selectedDates: newOpeningTime.selectedDays.map(day => {
                return new Date(newOpeningTime.year, newOpeningTime.month, day).toISOString().split('T')[0];
            })
        };

        axiosInstance.post("/openingTimes/create", requestBody)
            .then(() => {
                setFeedback("Nyitvatartási idők sikeresen hozzáadva.");
                setNewOpeningTime({
                    year: '',
                    month: '',
                    timeFrom: '',
                    timeTo: '',
                    selectedDays: [],
                });
                fetchOpeningTimes();
            })
            .catch((error) => {
                console.error("Hiba a nyitvatartási idők hozzáadásakor:", error);
                setFeedback("Hiba történt a nyitvatartási idők hozzáadásakor.");
            });
    };

    return (
        <>
            <div className="row">
                <div className="col">
                    <Link to="/admin">
                        <button>Vissza</button>
                    </Link>
                </div>
            </div>
            <div className="col-12">
                <h1 className="titleoflist">Nyitvatartás</h1>
            </div>

            {/* Létező nyitvatartási idők táblája */}
            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Nyitvatartás dátum</th>
                                <th>Nyitvatartás kezdete</th>
                                <th>Nyitvatartás vége</th>
                                <th>Műveletek</th>
                            </tr>
                            </thead>
                            <tbody>
                            {openingTimes.map((openingTime) => {
                                const isFutureDate = new Date(openingTime.date) > new Date();
                                const canDelete = isFutureDate && !isDateBooked(openingTime.date);
                                return (
                                    <tr className="rows" key={openingTime.openingTimeId}>
                                        <td>{openingTime.date}</td>
                                        <td>{openingTime.timeFrom}</td>
                                        <td>{openingTime.timeTo}</td>
                                        <td>
                                            {canDelete ? (
                                                <button onClick={() => handleDeleteOpeningTime(openingTime.openingTimeId)}>
                                                    Törlés
                                                </button>
                                            ) : (
                                                <span>Törlés nem lehetséges</span>
                                            )}
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {/* Gombok a hónapok váltásához */}
            <div className="row">
                <div className="col">
                    <button onClick={handlePreviousMonth}>Előző hónap</button>
                    <button onClick={handleNextMonth}>Következő hónap</button>
                </div>
            </div>

            {/* Új nyitvatartási idő hozzáadása */}
            <div className="new-opening-time-form">
                <h2>Új nyitvatartási idő hozzáadása</h2>
                <form onSubmit={handleAddOpeningTimes}>
                    <div>
                        <label>Év:</label>
                        <select
                            name="year"
                            value={newOpeningTime.year}
                            onChange={handleNewOpeningTimeChange}
                        >
                            <option value="">Válassz évet</option>
                            {[currentYear, currentYear + 1, currentYear + 2].map((year) => (
                                <option key={year} value={year}>
                                    {year}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label>Hónap:</label>
                        <select
                            name="month"
                            value={newOpeningTime.month}
                            onChange={(e) => handleNewOpeningTimeChange({ target: { name: 'month', value: parseInt(e.target.value) } })}
                        >
                            <option value="">Válassz hónapot</option>
                            {getAvailableMonths().map((month) => (
                                <option key={month} value={month}>
                                    {month + 1}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label>Napok:</label>
                        <div className="days-checkbox">
                            {newOpeningTime.year &&
                                newOpeningTime.month !== "" &&
                                getDaysInMonth(newOpeningTime.month, parseInt(newOpeningTime.year)).map((day) => (
                                    <div key={day}>
                                        <input
                                            type="checkbox"
                                            value={day}
                                            checked={newOpeningTime.selectedDays.includes(day)}
                                            onChange={() => handleDaySelection(day)}
                                        />
                                        {day}
                                    </div>
                                ))}
                        </div>
                    </div>
                    <div>
                        <label>Nyitás ideje:</label>
                        <input
                            type="time"
                            name="timeFrom"
                            value={newOpeningTime.timeFrom}
                            onChange={handleNewOpeningTimeChange}
                        />
                    </div>
                    <div>
                        <label>Zárás ideje:</label>
                        <input
                            type="time"
                            name="timeTo"
                            value={newOpeningTime.timeTo}
                            onChange={handleNewOpeningTimeChange}
                        />
                    </div>
                    <button type="submit">Hozzáadás</button>
                </form>
                {feedback && <p>{feedback}</p>}
            </div>
        </>
    );
}

export default OpeningTimeFilterPage;
