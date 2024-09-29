import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function ServiceFilterPage() {
    const [groupedServices, setGroupedServices] = useState({});
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        axiosInstance.get('/services/all-service-with-names')
            .then(response => {
                // Group services by provider's name and type
                const grouped = response.data.reduce((acc, service) => {
                    const providerKey = `${service.serviceProvider}`;
                    if (!acc[providerKey]) {
                        acc[providerKey] = [];
                    }
                    acc[providerKey].push(service);
                    return acc;
                }, {});
                setGroupedServices(grouped);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

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
                <h1 className="titleoflist">Szolgáltatások listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    {Object.keys(groupedServices).map(providerKey => (
                        <div key={providerKey} className="col-12 mb-4">
                            <h2>{providerKey}</h2>
                            <div className="table-responsive">
                                <table className="table table-striped table-hover custom-table">
                                    <thead>
                                    <tr>
                                        <th>Szolgáltatás név</th>
                                        <th>Szolgáltatás ára</th>
                                        <th>Szolgáltatás hossz</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {groupedServices[providerKey].map(service => (
                                        <tr key={service.serviceId}>
                                            <td>{service.serviceName}</td>
                                            <td>{service.servicePrice + " Ft"}</td>
                                            <td>{service.serviceLength + " perc"}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <div className="row">
                <div className="col">
                    <Link to="/admin">
                        <button>Vissza</button>
                    </Link>
                </div>
            </div>
        </>
    )
}

export default ServiceFilterPage;
