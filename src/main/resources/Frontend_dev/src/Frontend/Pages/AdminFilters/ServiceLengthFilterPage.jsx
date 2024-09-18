import {Link} from "react-router-dom";
import React, {useState, useEffect} from "react";
import "./Tables.css";
import axios from "axios";

function ServiceLengthFilterPage(){
    const [serviceLengths, setServiceLengths] = useState([]);

    useEffect( () => {
        axios.get('http://localhost:8080/api/serviceLengths', {
            auth: {
                username: 'admin', // Felhasználónév
                password: 'almafa' // Jelszó
            }
        })
            .then(response => {
                setServiceLengths(response.data);
                })
            .catch(error => {
                console.error('Error fetching data:', error)
            });
        }, []);

    return (
        <>
            <div className="col-12">
                <h1 className="titleoflist">Szolgáltatás idők listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                <div className="table-responsive">
                    <table className="table table-striped table-hover custom-table">
                        <thead>
                             <tr>
                                 <th>Szolgáltatás hossz azonosító</th>
                                 <th>Szolgáltatás hossz</th>
                             </tr>
                        </thead>
                        <tbody>
                            {serviceLengths.map(serviceLength => (
                                <tr className="rows" key={serviceLength.serviceLengthId}>
                                    <td>{serviceLength.serviceLengthId}</td>
                                    <td>{serviceLength.serviceLength}</td>
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

export default ServiceLengthFilterPage;