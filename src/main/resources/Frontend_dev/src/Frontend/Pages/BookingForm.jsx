import React, { useState, useEffect } from 'react';
import axiosInstance from "../../AxiosInterceptor";

function BookingForm() {
    const [serviceProviders, setServiceProviders] = useState([]);
    const [services, setServices] = useState([]);
    const [openingTimes, setOpeningTimes] = useState([]);
    const [selectedServiceProvider, setSelectedServiceProvider] = useState('');
    const [selectedService, setSelectedService] = useState('');
    const [year, setYear] = useState('');
    const [month, setMonth] = useState('');
    const [day, setDay] = useState('');
    const [time, setTime] = useState('');
    const [comment, setComment] = useState('');
    const [feedback, setFeedback] = useState('');
    const [selectedDayOpeningTime, setSelectedDayOpeningTime] = useState(null);
    const [bookedTimes, setBookedTimes] = useState([]);
    const userId = localStorage.getItem('userId');
    const loggedInServiceProviderId = localStorage.getItem('serviceProviderId'); // Get logged-in service provider's ID

    useEffect(() => {
        axiosInstance.get('/serviceProviders/all-serviceprovider')
            .then(response => {
                setServiceProviders(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                setFeedback('Hiba történt az adatok lekérésekor.');
            });
    }, []);

    useEffect(() => {
        if (selectedServiceProvider) {
            axiosInstance.get(`/bookings/service-provider/${selectedServiceProvider}`)
                .then(response => {
                    setServices(response.data);
                })
                .catch(error => {
                    console.error('Error fetching services:', error);
                    setFeedback('Nem sikerült a szolgáltatások lekérése.');
                });

            axiosInstance.get(`/openingTimes/${selectedServiceProvider}/opening-times`)
                .then(response => {
                    setOpeningTimes(response.data);
                    console.log('Opening times:', response.data);
                })
                .catch(error => {
                    console.error('Error fetching opening times:', error);
                    setFeedback('Nem sikerült a nyitvatartási időket lekérni.');
                });
        }
    }, [selectedServiceProvider]);

    const getYears = () => {
        const currentYear = new Date().getFullYear();
        return Array.from(new Set(openingTimes.map(ot => new Date(ot.date).getFullYear()))).filter(year => year >= currentYear);
    };

    const getMonths = () => {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        return Array.from(new Set(openingTimes
            .filter(ot => new Date(ot.date).getFullYear() === parseInt(year))
            .map(ot => new Date(ot.date).getMonth())
        )).filter(month => parseInt(year) > currentYear || month >= currentMonth);
    };

    const getDays = () => {
        return Array.from(new Set(openingTimes
            .filter(ot => {
                const date = new Date(ot.date);
                return date.getFullYear() === parseInt(year) && date.getMonth() === parseInt(month) - 1;
            })
            .map(ot => new Date(ot.date).getDate())
        ));
    };

    useEffect(() => {
        if (year && month && day) {
            const selectedDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

            const openingTimeForDay = openingTimes.find(ot => {
                const date = new Date(ot.date);
                return date.getFullYear() === parseInt(year) && date.getMonth() === parseInt(month) - 1 && date.getDate() === parseInt(day);
            });

            setSelectedDayOpeningTime(openingTimeForDay);

            axiosInstance.get(`/bookings/service-provider/${selectedServiceProvider}/bookings-on-date`, {
                params: {
                    date: selectedDate
                }
            })
                .then(response => {
                    setBookedTimes(response.data);
                })
                .catch(error => {
                    console.error('Error fetching booked times:', error);
                });
        }
    }, [year, month, day, selectedServiceProvider]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!selectedServiceProvider || !selectedService || !year || !month || !day || !time || !comment) {
            setFeedback('Kérlek, minden mezőt tölts ki!');
            return;
        }

        const selectedDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const selectedDateTime = new Date(`${selectedDate}T${time}`);
        const currentDateTime = new Date();

        if (selectedDateTime < currentDateTime) {
            setFeedback('A megadott időpont múltbeli időpont.');
            return;
        }

        const openingTimeForDay = openingTimes.find(ot => {
            const date = new Date(ot.date);
            return date.getFullYear() === parseInt(year) && date.getMonth() === parseInt(month) - 1 && date.getDate() === parseInt(day);
        });

        if (!openingTimeForDay) {
            setFeedback('A szolgáltató ezen a napon nem dolgozik.');
            return;
        }

        const openingStartTime = openingTimeForDay.timeFrom ? openingTimeForDay.timeFrom.substring(0, 5) : null;
        const openingEndTime = openingTimeForDay.timeTo ? openingTimeForDay.timeTo.substring(0, 5) : null;

        if (!openingStartTime || !openingEndTime) {
            console.error('Hiba: openingStartTime vagy openingEndTime hiányzik:', openingTimeForDay);
            setFeedback('Hiba történt a nyitvatartási idő betöltésekor.');
            return;
        }

        if (time < openingStartTime || time > openingEndTime) {
            setFeedback('A szolgáltató az adott időpontban zárva tart.');
            return;
        }

        const bookingRequest = {
            userId: userId,
            serviceProviderID: selectedServiceProvider,
            serviceId: selectedService,
            date: selectedDate,
            time,
            comment
        };

        axiosInstance.post('/bookings/create', bookingRequest)
            .then(response => {
                setFeedback('A foglalás sikeresen elküldve!');
            })
            .catch(error => {
                if (error.response) {
                    if (error.response.status === 409) {
                        setFeedback('Az idősáv már foglalt, sikertelen foglalás.');
                    } else {
                        setFeedback('Nem sikerült a foglalást elküldeni.');
                    }
                } else {
                    setFeedback('Nem sikerült a foglalást elküldeni.');
                }
            });
    };

    return (
        <div>
            {feedback && <p>{feedback}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Service Provider:</label>
                    <select value={selectedServiceProvider} onChange={e => setSelectedServiceProvider(e.target.value)}>
                        <option value="">Select service provider</option>
                        {serviceProviders
                            .filter(provider => loggedInServiceProviderId === '0d9b8997-21db-410a-ba83-02d132311073' || provider.serviceProviderId === loggedInServiceProviderId)
                            .map(provider => (
                                <option key={provider.serviceProviderId} value={provider.serviceProviderId}>
                                    {provider.serviceProviderName}
                                </option>
                            ))}
                    </select>
                </div>
                <div>
                    <label>Service:</label>
                    <select value={selectedService} onChange={e => setSelectedService(e.target.value)}>
                        <option value="">Select service</option>
                        {services.map(service => (
                            <option key={service.serviceId} value={service.serviceId}>{service.serviceName}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Year:</label>
                    <select value={year} onChange={e => setYear(e.target.value)}>
                        <option value="">Select year</option>
                        {getYears().map(year => (
                            <option key={year} value={year}>{year}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Month:</label>
                    <select value={month} onChange={e => setMonth(e.target.value)}>
                        <option value="">Select month</option>
                        {getMonths().map(month => (
                            <option key={month} value={month + 1}>{month + 1}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Day:</label>
                    <select value={day} onChange={e => setDay(e.target.value)}>
                        <option value="">Select day</option>
                        {getDays().map(day => (
                            <option key={day} value={day}>{day}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Time:</label>
                    <input type="time" value={time} onChange={e => setTime(e.target.value)} />
                </div>
                <div>
                    <label>Comment:</label>
                    <input type="text" value={comment} onChange={e => setComment(e.target.value)} />
                </div>
                <button type="submit">Book</button>
            </form>

            {selectedDayOpeningTime && (
                <div>
                    <p>A szolgáltató nyitvatartása a kiválasztott napon: {selectedDayOpeningTime.timeFrom} - {selectedDayOpeningTime.timeTo}.</p>
                </div>
            )}
            {bookedTimes.length > 0 && (
                <div>
                    <p>Foglalt időpontok:</p>
                    <ul>
                        {bookedTimes.map((time, index) => (
                            <li key={index}>{time}</li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}

export default BookingForm;
