import React, { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from "../../AxiosInterceptor";

function BookingForm() {
    const [serviceProviders, setServiceProviders] = useState([]);
    const [services, setServices] = useState([]);
    const [selectedServiceProvider, setSelectedServiceProvider] = useState('');
    const [selectedService, setSelectedService] = useState('');
    const [date, setDate] = useState('');
    const [time, setTime] = useState('');
    const [comment, setComment] = useState('');
    const [feedback, setFeedback] = useState('');
    const userId = localStorage.getItem('userId');


    useEffect(() => {
        axiosInstance.get('/serviceProviders/all-serviceprovider')
            .then(response => {
                setServiceProviders(response.data);

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

    useEffect(() => {
        if (selectedServiceProvider) {
            axiosInstance.get(`/bookings/service-provider/${selectedServiceProvider}`)
                .then(response => {
                    setServices(response.data);
                })
                .catch(error => console.error('Error fetching services:', error));
        }
    }, [selectedServiceProvider]);

    const handleSubmit = (e) => {
        e.preventDefault();


        if (!selectedServiceProvider || !selectedService || !date || !time || !comment) {
            alert('Kérlek minden mezőt tölts ki!');
            return;
        }

        const bookingRequest = {
            userId: userId,
            serviceProviderID: selectedServiceProvider,
            serviceId: selectedService,
            date,
            time,
            comment
        };

        console.log('Booking Request:', bookingRequest);

        axiosInstance.post('/bookings/create', bookingRequest)
            .then(response => alert(response.data))
            .catch(error => {
                if (error.response && error.response.status === 403) {
                    setFeedback('Hozzáférés megtagadva. Kérlek, jelentkezz be újra.');
                } else {
                    setFeedback('Nem sikerült a foglalást elküldeni: ' + (error.response?.data || error.message));
                }
            });
    };

    const getUserId = () => {
        return localStorage.getItem('userId');
    };

    return (

        <form onSubmit={handleSubmit}>
            <div>
                <label>Service Provider:</label>
                <select value={selectedServiceProvider} onChange={e => setSelectedServiceProvider(e.target.value)}>
                    <option value="">Select service provider</option>
                    {serviceProviders.map(provider => (
                        <option key={provider.serviceProviderId} value={provider.serviceProviderId}>{provider.serviceProviderName}</option>
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
                <label>Date:</label>
                <input type="date" value={date} onChange={e => setDate(e.target.value)} />
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

    );

}

export default BookingForm;
