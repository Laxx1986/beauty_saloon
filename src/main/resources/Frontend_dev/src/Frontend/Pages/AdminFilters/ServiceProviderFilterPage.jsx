import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import './Tables.css';
import axiosInstance from "../../../AxiosInterceptor";

function ServiceProviderFilterPage() {
    const [serviceProviders, setServiceProviders] = useState([]);
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        axiosInstance.get('/serviceProviders/with-users')  .then(response => {
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

    return (
        <>
            <div className="col-12">
                <h1 className="titleoflist">Szolgáltatók listája</h1>
            </div>
            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Szolgáltató név</th>
                                <th>Felhasználó név</th>
                                <th>Név</th>
                            </tr>
                            </thead>
                            <tbody>
                            {serviceProviders.map(serviceProvider => (
                                <tr className="rows" key={serviceProvider.serviceProviderName + serviceProvider.userName}>
                                    <td>{serviceProvider.serviceProviderName}</td>
                                    <td>{serviceProvider.userName}</td>
                                    <td>{serviceProvider.name}</td>
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
    )
}

export default ServiceProviderFilterPage;
