import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import "./Tables.css";
import axiosInstance from "../../../AxiosInterceptor";

function ServiceFilterPage() {
    const [groupedServices, setGroupedServices] = useState({});
    const [feedback, setFeedback] = useState('');
    const [newService, setNewService] = useState({
        serviceName: '',
        servicePrice: '',
        serviceLengthId: '',
        serviceProviderId: ''
    });
    const [serviceLengths, setServiceLengths] = useState([]);
    const [serviceProviders, setServiceProviders] = useState([]);
    const [editingService, setEditingService] = useState(null);
    const loggedInServiceProviderId = localStorage.getItem('serviceProviderId'); // Get logged-in service provider's ID

    console.log(loggedInServiceProviderId);
    useEffect(() => {
        fetchServices();

        // Fetch service lengths and providers
        axiosInstance.get('/serviceLengths/getlengths')
            .then(response => setServiceLengths(response.data))
            .catch(error => console.error('Error fetching service lengths:', error));

        axiosInstance.get('/serviceProviders/all-serviceprovider')
            .then(response => setServiceProviders(response.data))
            .catch(error => console.error('Error fetching service providers:', error));
    }, []);

    const fetchServices = () => {
        axiosInstance.get('/services/all-service-with-names-for-table')
            .then(response => {
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
    };

    // Add a new service
    const handleAddService = (e) => {
        e.preventDefault();

        if (!newService.serviceName || !newService.servicePrice || !newService.serviceLengthId || !newService.serviceProviderId) {
            setFeedback('Kérjük, töltsd ki az összes mezőt!');
            return;
        }

        axiosInstance.post('/services/add', newService)
            .then(response => {
                setFeedback('Szolgáltatás sikeresen hozzáadva.');
                setNewService({ serviceName: '', servicePrice: '', serviceLengthId: '', serviceProviderId: '' });
                fetchServices(); // Refresh the list
            })
            .catch(error => {
                console.error('Error adding service:', error);
                setFeedback('Hiba történt a szolgáltatás hozzáadásakor.');
            });
    };

    // Delete a service
    const handleDeleteService = (serviceId) => {
        axiosInstance.delete(`/services/delete/${serviceId}`)
            .then(response => {
                setFeedback('Szolgáltatás sikeresen törölve.');
                fetchServices(); // Refresh the list
            })
            .catch(error => {
                console.error('Error deleting service:', error);
                setFeedback('Hiba történt a szolgáltatás törlésekor.');
            });
    };

    // Update service
    const handleUpdateService = (e) => {
        e.preventDefault();

        if (!editingService.servicePrice || !editingService.serviceLengthId) {
            setFeedback('Kérjük, töltsd ki az összes mezőt!');
            return;
        }

        axiosInstance.put(`/services/update/${editingService.serviceId}`, editingService)
            .then(response => {
                setFeedback('Szolgáltatás sikeresen módosítva.');
                setEditingService(null);
                fetchServices(); // Refresh the list
            })
            .catch(error => {
                console.error('Error updating service:', error);
                setFeedback('Hiba történt a szolgáltatás módosításakor.');
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
                <h1 className="titleoflist">Szolgáltatások listája</h1>
            </div>

            <div className="container col-12">
                <div className="row">
                    {Object.keys(groupedServices).map(providerKey => (
                        <div key={providerKey} className="col-12 mb-4">
                            <h2>{providerKey}</h2>
                            <div className="table-responsive">
                                {feedback && <p>{feedback}</p>}
                                <table className="table table-striped table-hover custom-table">
                                    <thead>
                                    <tr>
                                        <th>Szolgáltatás név</th>
                                        <th>Szolgáltatás ára</th>
                                        <th>Szolgáltatás hossz</th>
                                        <th>Műveletek</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {groupedServices[providerKey].map(service => (
                                        <tr key={service.serviceId}>
                                            <td>{service.serviceName}</td>
                                            <td>
                                                {editingService && editingService.serviceId === service.serviceId ? (
                                                    <input
                                                        type="number"
                                                        value={editingService.servicePrice}
                                                        onChange={(e) => setEditingService({
                                                            ...editingService,
                                                            servicePrice: e.target.value
                                                        })}
                                                    />
                                                ) : (
                                                    `${service.servicePrice} Ft`
                                                )}
                                            </td>
                                            <td>
                                                {editingService && editingService.serviceId === service.serviceId ? (
                                                    <select
                                                        value={editingService.serviceLengthId || ''} // Ensure the default value is an empty string
                                                        onChange={(e) => setEditingService({
                                                            ...editingService,
                                                            serviceLengthId: e.target.value
                                                        })}
                                                    >
                                                        <option value="">Kérjük, válassz</option>
                                                        {/* Add the prompt option */}
                                                        {serviceLengths.map(length => (
                                                            <option key={length.serviceLengthId}
                                                                    value={length.serviceLengthId}>
                                                                {length.serviceLength} perc
                                                            </option>
                                                        ))}
                                                    </select>
                                                ) : (
                                                    `${service.serviceLength} perc`
                                                )}
                                            </td>
                                            <td>
                                                {(service.serviceProviderId === loggedInServiceProviderId || loggedInServiceProviderId === '0d9b8997-21db-410a-ba83-02d132311073') && (
                                                    <>
                                                        {editingService && editingService.serviceId === service.serviceId ? (
                                                            <>
                                                                <button onClick={handleUpdateService}>Mentés</button>
                                                                <button onClick={() => setEditingService(null)}>Mégsem
                                                                </button>
                                                            </>
                                                        ) : (
                                                            <>
                                                                <button
                                                                    onClick={() => setEditingService(service)}>Módosítás
                                                                </button>
                                                                <button
                                                                    onClick={() => handleDeleteService(service.serviceId)}>Törlés
                                                                </button>
                                                            </>
                                                        )}
                                                    </>
                                                )}
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Új szolgáltatás hozzáadása */}
                <div className="row">
                    <div className="col">
                        <h2>Új szolgáltatás hozzáadása</h2>
                        <form onSubmit={handleAddService}>
                            <div>
                                <label>Szolgáltatás név:</label>
                                <input
                                    type="text"
                                    value={newService.serviceName}
                                    onChange={(e) => setNewService({...newService, serviceName: e.target.value})}
                                    required
                                />
                            </div>
                            <div>
                                <label>Szolgáltatás ára:</label>
                                <input
                                    type="number"
                                    value={newService.servicePrice}
                                    onChange={(e) => setNewService({...newService, servicePrice: e.target.value})}
                                    required
                                />
                            </div>
                            <div>
                                <label>Szolgáltatás hossz:</label>
                                <select
                                    value={newService.serviceLengthId || ''} // Ensure the default value is an empty string
                                    onChange={(e) => setNewService({...newService, serviceLengthId: e.target.value})}
                                    required
                                >
                                    <option value="">Kérjük, válassz</option>
                                    {serviceLengths.map(length => (
                                        <option key={length.serviceLengthId} value={length.serviceLengthId}>
                                            {length.serviceLength} perc
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <label>Szolgáltató:</label>
                                <select
                                    value={newService.serviceProviderId}
                                    onChange={(e) => setNewService({...newService, serviceProviderId: e.target.value})}
                                    required
                                >
                                    <option value="">Válassz szolgáltatót</option>
                                    {serviceProviders
                                        .filter(provider => loggedInServiceProviderId === '0d9b8997-21db-410a-ba83-02d132311073' || provider.serviceProviderId === loggedInServiceProviderId)
                                        .map(provider => (
                                            <option key={provider.serviceProviderId} value={provider.serviceProviderId}>
                                                {provider.serviceProviderName}
                                            </option>
                                        ))}
                                </select>
                            </div>
                            <button type="submit">Hozzáadás</button>
                        </form>
                    </div>
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
    );
}

export default ServiceFilterPage;
