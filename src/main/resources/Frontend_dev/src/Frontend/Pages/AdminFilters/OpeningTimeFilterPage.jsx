import {Link} from "react-router-dom";
import React, {useState, useEffect} from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function OpeningTimeFilterPage() {
    const [openingTimes, setOpeningTimes] = useState([]);
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        axiosInstance.get("/openingTimes") .then(response => {
            setOpeningTimes(response.data);

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
            <div className="row">
                <div className="col">
                    <Link to="/admin">
                        <button>Vissza</button>
                    </Link>
                </div>
            </div>
            <div className="col-12">
                <h1 className="titleoflist">Nyitvatartás</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Nyitvatartás dátum</th>
                                <th>Nyitvatartás kezdete</th>
                                <th>Nyitvatartás Vége</th>
                                <th>Felhasználó név</th>
                                <th>Szolgáltató név</th>
                            </tr>
                            </thead>
                            <tbody>
                            {openingTimes.map(openingTime => (
                                <tr className="rows" key={openingTime.openingTimeId}>
                                    <td>{openingTime.date}</td>
                                    <td>{openingTime.timeFrom}</td>
                                    <td>{openingTime.timeTo}</td>
                                    <td>{openingTime.userName}</td>
                                    <td>{openingTime.name}</td>
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