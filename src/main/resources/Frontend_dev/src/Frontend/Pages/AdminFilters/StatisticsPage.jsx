import React, { useState, useEffect } from 'react';
import axiosInstance from "../../../AxiosInterceptor";

function StatisticsPage() {
    const [statistics, setStatistics] = useState([]);
    const [year, setYear] = useState(new Date().getFullYear());
    const [month, setMonth] = useState(new Date().getMonth() + 1);

    useEffect(() => {
        fetchStatistics(year, month);
    }, [year, month]);

    const fetchStatistics = (year, month) => {
        axiosInstance.get(`/statistics/monthly?year=${year}&month=${month}`)
            .then(response => {
                setStatistics(response.data);
            })
            .catch(error => {
                console.error('Error fetching statistics:', error);
            });
    };

    const handleMonthChange = (direction) => {
        let newMonth = month + direction;
        let newYear = year;

        if (newMonth > 12) {
            newMonth = 1;
            newYear += 1;
        } else if (newMonth < 1) {
            newMonth = 12;
            newYear -= 1;
        }

        setYear(newYear);
        setMonth(newMonth);
    };

    return (
        <div>
            <h1>Statisztika</h1>
            <button onClick={() => handleMonthChange(-1)}>Előző hónap</button>
            <button onClick={() => handleMonthChange(1)}>Következő hónap</button>
            <h2>{year} - {month}</h2>

            {statistics.map((stat, index) => (
                <div key={index}>
                    <h3>{stat.serviceProviderName}</h3>
                    <table>
                        <thead>
                        <tr>
                            <th>Név</th>
                            <th>Szolgáltatás</th>
                            <th>Időpont (hónap)</th>
                            <th>Időpont (nap)</th>
                            <th>Szolgáltatás ára</th>
                        </tr>
                        </thead>
                        <tbody>
                        {stat.bookings.map((booking, idx) => (
                            <tr key={idx}>
                                <td>{booking.userName}</td>
                                <td>{booking.serviceName}</td>
                                <td>{booking.bookingMonth}</td>
                                <td>{booking.bookingDay}</td>
                                <td>{booking.servicePrice} Ft</td>
                            </tr>
                        ))}
                        <tr>
                            <td colSpan="4"><strong>Összes bevétel:</strong></td>
                            <td><strong>{stat.totalIncome} Ft</strong></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            ))}
        </div>
    );
}

export default StatisticsPage;
