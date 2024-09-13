import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axios from "axios";

function ServiceFilterPage() {
    const [services, setServices] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8080/api/services/all-service", {
            auth: {
                username: 'admin', // Felhasználónév
                password: 'almafa' // Jelszó
            }
        })
            .then(response => {
                setServices(response.data);
                console.log(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error)
            });
    }, []);

    return (
        <>
            <div className="col-12">
                <h1 className="titleoflist">Szolgáltatások listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Szolgáltatás azonosító</th>
                                <th>Szolgáltatás név</th>
                                <th>Szolgáltatás ára</th>
                                <th>Szolgáltatás hossz</th>
                                <th>Szolgáltató típus</th>
                            </tr>
                            </thead>
                            <tbody>
                            {services.map(service => (
                                <tr className="rows" key={service.serviceId}>
                                    <td>{service.serviceId}</td>
                                    <td>{service.serviceName}</td>
                                    <td>{service.servicePrice + " Ft"}</td>
                                    <td>{service.serviceLength + " perc"}</td>
                                    <td>{service.serviceProvider}</td>
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

export default ServiceFilterPage;
