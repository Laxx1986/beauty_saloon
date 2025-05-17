import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import axiosInstance from "../../AxiosInterceptor";
import BookingForm from './BookingForm';
import 'react-big-calendar/lib/css/react-big-calendar.css';

const localizer = momentLocalizer(moment);

function MyCalendar() {
    const [events, setEvents] = useState([]);
    const [showBookingForm, setShowBookingForm] = useState(false);
    const [userId, setUserId] = useState('');
    const [userRole, setUserRole] = useState('');

    useEffect(() => {
        // Fetch the user's ID and role
        axiosInstance.get('/users/me')
            .then(response => {
                setUserId(response.data.userId);
                setUserRole(response.data.role);
            })
            .catch(error => {
                console.error('Error fetching user information:', error);
            });
    }, []);

    useEffect(() => {
        if (!userId || !userRole) return; // Wait until both userId and userRole are fetched

        axiosInstance.get('/bookings/all-booking')
            .then(response => {
                const bookings = response.data.map(booking => {
                    const { bookingId, date, time, comment, userId: bookingUserId, serviceProviderName, serviceName, serviceLength } = booking;
                    const startDate = new Date(date);
                    if (!isNaN(startDate.getTime()) && time) { // Check if startDate is a valid date and time is provided
                        const [hours, minutes, seconds] = time.split(':');
                        startDate.setHours(hours || 0);
                        startDate.setMinutes(minutes || 0);
                        startDate.setSeconds(seconds || 0);
                    }

                    const endDate = new Date(startDate);
                    if (!isNaN(endDate.getTime())) {
                        endDate.setMinutes(startDate.getMinutes() + (serviceLength || 0)); // Use default 0 if serviceLength is missing
                    }

                    // Check if the booking belongs to the logged-in user or if the user has the special ID
                    const isCurrentUserBooking = bookingUserId === userId;
                    const showAllDetails = userId === 'b0b58a07-c8df-452c-a215-cec93d404870';

                    return {
                        id: bookingId,
                        title: showAllDetails || isCurrentUserBooking ? `${serviceProviderName} - ${serviceName} - ${comment}` : '',
                        start: startDate,
                        end: endDate,
                        serviceProviderName,
                        serviceName,
                        isCurrentUserBooking,
                        showAllDetails
                    };
                });
                setEvents(bookings);
            })
            .catch(error => {
                console.error('Error fetching bookings:', error);
            });
    }, [userId, userRole]);

    const eventStyleGetter = (event) => {
        let backgroundColor = 'blue';
        let color = 'white';

        if (!event.showAllDetails && !event.isCurrentUserBooking) {
            backgroundColor = 'gray'; // Use a default color for other users' bookings
        } else {
            // Customize color based on the service provider
            switch (event.serviceProviderName) {
                case 'Mukormos':
                    backgroundColor = 'red';
                    break;
                case 'Fodrasz1':
                    backgroundColor = 'green';
                    break;
                case 'Fodrasz2':
                    backgroundColor = 'yellow';
                    color = 'black';
                    break;
                case 'Kozmetikus':
                    backgroundColor = 'purple';
                    break;
                default:
                    backgroundColor = 'blue';
            }
        }

        return {
            style: {
                backgroundColor,
                color: color,
                borderRadius: '2px',
                border: '2px',
            }
        };
    };

    return (
        <div>
            <button onClick={() => setShowBookingForm(!showBookingForm)}>Új foglalás</button>
            {showBookingForm && <BookingForm />}
            <Calendar
                localizer={localizer}
                events={events}
                startAccessor="start"
                endAccessor="end"
                style={{ height: 500 }}
                eventPropGetter={eventStyleGetter}
            />
        </div>
    );
}

export default MyCalendar;
