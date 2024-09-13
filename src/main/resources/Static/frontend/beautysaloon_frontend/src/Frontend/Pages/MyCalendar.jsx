import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import axios from 'axios';
import BookingForm from './BookingForm';
import 'react-big-calendar/lib/css/react-big-calendar.css';

const localizer = momentLocalizer(moment);

function MyCalendar() {
    const [events, setEvents] = useState([]);
    const [showBookingForm, setShowBookingForm] = useState(false);

    useEffect(() => {
        axios.get('http://localhost:8080/api/bookings/all-booking')
            .then(response => {
                const bookings = response.data.map(booking => {
                    const [id, date, time, comment, userName, serviceProviderName, serviceName, serviceLength] = booking;
                    const startDate = new Date(date);
                    const [hours, minutes, seconds] = time.split(':');
                    startDate.setHours(hours);
                    startDate.setMinutes(minutes);
                    startDate.setSeconds(seconds);

                    const endDate = new Date(startDate);
                    endDate.setMinutes(startDate.getMinutes() + serviceLength);

                    return {
                        id,
                        title: `${serviceProviderName} - ${serviceName} - ${comment}`,
                        start: startDate,
                        end: endDate,
                        userName,
                        serviceProviderName,
                        serviceName,
                        comment,
                        serviceLength
                    };
                });
                setEvents(bookings);
            })
            .catch(error => {
                console.error('Error fetching bookings:', error);
            });
    }, []);

    const eventStyleGetter = (event) => {
        let backgroundColor = 'blue';
        let color = 'white';

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

        return {
            style: {
                backgroundColor,
                color: color,
                borderRadius: '2px',
                border: '2px'
            }
        };
    };

    return (
        <div>
            <button onClick={() => setShowBookingForm(!showBookingForm)}>New Booking</button>
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
