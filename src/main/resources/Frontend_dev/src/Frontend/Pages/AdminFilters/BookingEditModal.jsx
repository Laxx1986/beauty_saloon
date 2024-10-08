import React, { useState, useEffect } from "react";
import axiosInstance from "../../../AxiosInterceptor";

function BookingEditModal({ bookingId, closeModal, serviceProviderId, userId, userRole }) {
    const [booking, setBooking] = useState({
        year: "",
        month: "",
        day: "",
        time: "",
        comment: "",
        serviceId: "",
        selectedServiceProviderId: serviceProviderId,
    });
    const [services, setServices] = useState([]);
    const [serviceProviders, setServiceProviders] = useState([]);
    const [openingTimes, setOpeningTimes] = useState([]);
    const SPECIAL_SERVICE_PROVIDER_ID = "0d9b8997-21db-410a-ba83-02d132311073"; // ID that allows access to all services

    useEffect(() => {
        if (bookingId) {
            fetchBooking();
        }
        if (serviceProviderId === SPECIAL_SERVICE_PROVIDER_ID) {
            fetchAllServiceProviders();
        }
    }, [bookingId, serviceProviderId]);

    const fetchBooking = () => {
        axiosInstance
            .get(`/bookings/${bookingId}`)
            .then((response) => {
                const date = new Date(response.data.date);
                setBooking({
                    ...response.data,
                    year: date.getFullYear(),
                    month: date.getMonth() + 1,
                    day: date.getDate(),
                    selectedServiceProviderId: serviceProviderId,
                });
                fetchServices(serviceProviderId);
                fetchOpeningTimes(serviceProviderId);
            })
            .catch((error) => {
                console.error("Error fetching booking:", error);
            });
    };

    const fetchServices = (providerId) => {
        if (!providerId) {
            console.error('ServiceProviderId is undefined in fetchServices!');
            return;
        }

        const url = providerId === SPECIAL_SERVICE_PROVIDER_ID
            ? '/services/all-service'
            : `/bookings/service-provider/${providerId}`;

        axiosInstance
            .get(url)
            .then((response) => {
                setServices(response.data);
            })
            .catch((error) => {
                console.error("Error fetching services:", error);
            });
    };

    const fetchAllServiceProviders = () => {
        axiosInstance
            .get('/serviceProviders/all-serviceprovider')
            .then((response) => {
                setServiceProviders(response.data);
            })
            .catch((error) => {
                console.error("Error fetching service providers:", error);
            });
    };

    const fetchOpeningTimes = (providerId) => {
        if (!providerId) {
            console.error('ServiceProviderId is undefined in fetchOpeningTimes!');
            return;
        }

        axiosInstance
            .get(`/openingTimes/${providerId}/opening-times`)
            .then((response) => {
                setOpeningTimes(response.data);
            })
            .catch((error) => {
                console.error("Error fetching opening times:", error);
            });
    };

    const getYears = () => {
        const currentYear = new Date().getFullYear();
        return Array.from(new Set(openingTimes.map(ot => new Date(ot.date).getFullYear()))).filter(year => year >= currentYear);
    };

    const getMonths = () => {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        return Array.from(new Set(openingTimes
            .filter(ot => new Date(ot.date).getFullYear() === parseInt(booking.year))
            .map(ot => new Date(ot.date).getMonth() + 1)
        )).filter(month => parseInt(booking.year) > currentYear || month >= currentMonth + 1);
    };

    const getDays = () => {
        return Array.from(new Set(openingTimes
            .filter(ot => {
                const date = new Date(ot.date);
                return date.getFullYear() === parseInt(booking.year) && date.getMonth() + 1 === parseInt(booking.month);
            })
            .map(ot => new Date(ot.date).getDate())
        ));
    };

    const handleChange = (e) => {
        let { name, value } = e.target;
        if (name === "time" && value.length > 5) {
            value = value.substring(0, 5);
        }

        setBooking({
            ...booking,
            [name]: value
        });
    };

    const handleServiceProviderChange = (e) => {
        const selectedProviderId = e.target.value;
        setBooking({
            ...booking,
            selectedServiceProviderId: selectedProviderId,
        });
        fetchServices(selectedProviderId);
        fetchOpeningTimes(selectedProviderId);
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const selectedDate = `${booking.year}-${String(booking.month).padStart(2, '0')}-${String(booking.day).padStart(2, '0')}`;
        const selectedDateTime = new Date(`${selectedDate}T${booking.time}`);
        const currentDateTime = new Date();

        if (selectedDateTime < currentDateTime) {
            alert('A megadott időpont múltbeli időpont.');
            return;
        }

        const openingTimeForDay = openingTimes.find(ot => {
            const date = new Date(ot.date);
            return date.getFullYear() === parseInt(booking.year) && date.getMonth() + 1 === parseInt(booking.month) && date.getDate() === parseInt(booking.day);
        });

        if (!openingTimeForDay) {
            alert('A szolgáltató ezen a napon nem dolgozik.');
            return;
        }

        const openingStartTime = openingTimeForDay.timeFrom ? openingTimeForDay.timeFrom.substring(0, 5) : null;
        const openingEndTime = openingTimeForDay.timeTo ? openingTimeForDay.timeTo.substring(0, 5) : null;

        if (!openingStartTime || !openingEndTime) {
            alert('Hiba történt a nyitvatartási idő betöltésekor.');
            return;
        }

        if (booking.time < openingStartTime || booking.time > openingEndTime) {
            alert('A szolgáltató az adott időpontban zárva tart.');
            return;
        }

        const updatedBooking = {
            userId: userId,
            serviceProviderID: booking.selectedServiceProviderId,
            serviceId: booking.serviceId,
            date: selectedDate,
            time: booking.time,
            comment: booking.comment
        };

        axiosInstance
            .put(`/bookings/update/${bookingId}`, updatedBooking)
            .then(() => {
                alert("Foglalás sikeresen módosítva.");
                closeModal();
            })
            .catch((error) => {
                if (error.response) {
                    if (error.response.status === 409) {
                        alert('Az idősáv már foglalt, sikertelen módosítás.');
                    } else if (error.response.status === 400) {
                        alert('A foglalás a szolgáltató nyitvatartási idején túl van!');
                    } else {
                        alert('Nem sikerült a foglalás módosítása.');
                    }
                } else {
                    alert('Nem sikerült a foglalás módosítása.');
                }
                console.error("Error updating booking:", error);
            });
    };

    return (
        <div className="modal">
            <h2>Foglalás Módosítása</h2>
            <form onSubmit={handleSubmit}>
                {userRole === 'Admin' || serviceProviderId === SPECIAL_SERVICE_PROVIDER_ID ? (
                    <label>
                        Szolgáltató:
                        <select
                            name="selectedServiceProviderId"
                            value={booking.selectedServiceProviderId}
                            onChange={handleServiceProviderChange}
                        >
                            <option value="">Válassz szolgáltatót</option>
                            {serviceProviders.map((provider) => (
                                <option key={provider.serviceProviderId} value={provider.serviceProviderId}>
                                    {provider.serviceProviderName}
                                </option>
                            ))}
                        </select>
                    </label>
                ) : null}
                <br />
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
                    Év:
                    <select name="year" value={booking.year} onChange={handleChange}>
                        <option value="">Válassz évet</option>
                        {getYears().map((year) => (
                            <option key={year} value={year}>{year}</option>
                        ))}
                    </select>
                </label>
                <br />
                <label>
                    Hónap:
                    <select name="month" value={booking.month} onChange={handleChange}>
                        <option value="">Válassz hónapot</option>
                        {getMonths().map((month) => (
                            <option key={month} value={month}>{month}</option>
                        ))}
                    </select>
                </label>
                <br />
                <label>
                    Nap:
                    <select name="day" value={booking.day} onChange={handleChange}>
                        <option value="">Válassz napot</option>
                        {getDays().map((day) => (
                            <option key={day} value={day}>{day}</option>
                        ))}
                    </select>
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
