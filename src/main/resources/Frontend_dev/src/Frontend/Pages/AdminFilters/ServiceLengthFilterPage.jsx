import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function ServiceLengthFilterPage() {
    const [serviceLengths, setServiceLengths] = useState([]);
    const [feedback, setFeedback] = useState('');
    const [newServiceLength, setNewServiceLength] = useState('');

    useEffect(() => {
        fetchServiceLengths();
    }, []);

    // Adatok lekérése a backendből
    const fetchServiceLengths = () => {
        axiosInstance.get('/serviceLengths/getlengths')
            .then(response => {
                // Az adatok rendezése szolgáltatás hossz szerint növekvő sorrendben
                const sortedServiceLengths = response.data.sort((a, b) => a.serviceLength - b.serviceLength);
                setServiceLengths(sortedServiceLengths);
                setFeedback(''); // Töröljük a hibaüzenetet
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                if (error.response && error.response.status === 403) {
                    setFeedback('Hozzáférés megtagadva. Kérlek, jelentkezz be újra.');
                } else {
                    setFeedback('Hiba történt az adatok lekérésekor.');
                }
            });
    };

    const handleDeleteServiceLength = (serviceLengthId) => {
        const confirmed = window.confirm("Biztosan törölni akarod?");
        if (confirmed) {
            axiosInstance.delete(`/serviceLengths/delete/${serviceLengthId}`)
                .then(response => {
                    alert('Sikeresen törölve.');
                    fetchServiceLengths(); // Frissítjük a listát a törlés után
                })
                .catch(error => {
                    console.error('Error deleting service length:', error);
                    alert('A szolgáltatás időtartama nem törölhető, mert használatban van egy szolgáltatásnál!');
                });
        } else {
            setFeedback('Törlés megszakítva.');
        }
    };

    // Új szolgáltatás hossz hozzáadása
    const handleAddServiceLength = (e) => {
        e.preventDefault();

        if (!newServiceLength) {
            setFeedback('Kérjük, adja meg a szolgáltatás hosszát!');
            return;
        }

        const serviceLengthDTO = {
            serviceLength: parseInt(newServiceLength),
        };

        axiosInstance.post('/serviceLengths/add', serviceLengthDTO)
            .then(response => {
                setFeedback('Szolgáltatás hossz sikeresen hozzáadva.');
                setNewServiceLength(''); // Töröljük a bemeneti mezőt
                fetchServiceLengths(); // Frissítjük a listát
            })
            .catch(error => {
                if (error.response && error.response.status === 400) {
                    alert('Ilyen időtartam már létezik, adjon meg mást!');
                } else {
                    console.error('Error adding service length:', error);
                    setFeedback('Hiba történt a szolgáltatás hossz hozzáadásakor.');
                }
            });
    };

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
                <h1 className="titleoflist">Szolgáltatás idők listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Szolgáltatás hossz</th>
                                <th>Műveletek</th>
                            </tr>
                            </thead>
                            <tbody>
                            {serviceLengths.map(serviceLength => (
                                <tr className="rows" key={serviceLength.serviceLengthId}>
                                    <td>{serviceLength.serviceLength} perc</td>
                                    <td>
                                        <button
                                            onClick={() => handleDeleteServiceLength(serviceLength.serviceLengthId)}>
                                            Törlés
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Új szolgáltatás hossz hozzáadása */}
                <div className="row">
                    <div className="col">
                        <h2>Új szolgáltatás hossz hozzáadása</h2>
                        <form onSubmit={handleAddServiceLength}>
                            <div>
                                <label>Szolgáltatás hossz (perc):</label>
                                <input
                                    type="number"
                                    value={newServiceLength}
                                    onChange={(e) => setNewServiceLength(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="submit">Hozzáadás</button>
                        </form>
                        {feedback && <p>{feedback}</p>}
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
    );
}

export default ServiceLengthFilterPage;
