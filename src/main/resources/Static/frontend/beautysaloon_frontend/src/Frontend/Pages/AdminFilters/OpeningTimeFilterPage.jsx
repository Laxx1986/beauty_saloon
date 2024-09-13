import {Link} from "react-router-dom";
import React, {useState, useEffect} from "react";
import "./Tables.css";
import axios from "axios";

function OpeningTimeFilterPage() {
    const [openingTimes, setOpeningTimes] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8080/api/openingTimes", {
            auth: {
                username: 'admin', // Felhasználónév
                password: 'almafa' // Jelszó
            }
        })
            .then(response => {
                setOpeningTimes(response.data)
            })
            .catch(error => {
                console.error('Error fetching data:', error)
            });
    })

    return (
        <>
            <div className="col-12">
                <h1 className="titleoflist">Nyitvatartás</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Nyitvatartás azonosító</th>
                                <th>Nyitvatartás dátum</th>
                                <th>Nyitvatartás kezdete</th>
                                <th>Nyitvatartás Vége</th>
                                <th>Szolgáltató azonosító</th>
                            </tr>
                            </thead>
                            <tbody>
                            {openingTimes.map(openingTime => (
                                <tr className="rows" key={"openingTime.openingTimeId"}>
                                    <td>{openingTime.openingTimeId}</td>
                                    <td>{openingTime.date}</td>
                                    <td>{openingTime.timeFrom}</td>
                                    <td>{openingTime.timeTo}</td>
                                    <td>{openingTime.serviceProviderId}</td>
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

export default OpeningTimeFilterPage;