import React, { useState, useEffect } from "react";
import axiosInstance from "../../AxiosInterceptor"; // Győződj meg róla, hogy az axiosInstance helyesen van importálva

function PriceListPage() {
    const [services, setServices] = useState([]);

    useEffect(() => {
        axiosInstance
            .get("/services/all-service-with-names")
            .then((response) => {
                setServices(response.data);
            })
            .catch((error) => {
                console.error("There was an error fetching the services!", error);
            });
    }, []);

    return (
        <>
            <h1>Price List</h1>
            <table>
                <thead>
                <tr>
                    <th>Szolgáltatás</th>
                    <th>Ár</th>
                    <th>Szolgáltatás időtartama</th>
                    <th>Szolgáltató</th>
                </tr>
                </thead>
                <tbody>
                {services.map((service) => (
                    <tr key={service.serviceId}>
                        <td>{service.serviceName}</td>
                        <td>{service.servicePrice}</td>
                        <td>{service.serviceLength}</td>
                        <td>{service.serviceProvider}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
}

export default PriceListPage;
