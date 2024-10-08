import { Link } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../AxiosInterceptor';
import UpdateUserModal from './UpdateUserModal'; // Importáljuk az új modális komponenst
import './Tables.css';

function UserFilterPage() {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [feedback, setFeedback] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = () => {
        axiosInstance.get('/users/all-users')
            .then(response => {
                setUsers(response.data);
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

    const handleDelete = (userId) => {
        axiosInstance.delete(`/users/${userId}`)
            .then(response => {
                setUsers(users.filter(user => user.userId !== userId));
                setFeedback('A felhasználó törölve lett.');
            })
            .catch(error => {
                console.error('Error deleting user:', error);
                setFeedback('Hiba történt a felhasználó törlésekor.');
            });
    };

    const confirmDeleteUser = (userId, userName, name) => {
        const confirmation = window.confirm(`Biztosan törölni akarod a ${userName}, ${name} felhasználót?`);
        if (confirmation) {
            handleDelete(userId);
        }
    };

    const openModal = (user) => {
        setSelectedUser(user);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedUser(null);
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
                <h1 className="titleoflist">Regisztrált felhasználók listája</h1>
                {feedback && <p className="feedback">{feedback}</p>}
            </div>
            <div className="container col-12">
                <div className="row">
                    <div className="table-responsive">
                        <table className="table table-striped table-hover custom-table">
                            <thead>
                            <tr>
                                <th>Név</th>
                                <th>Felhasználó név</th>
                                <th>Email</th>
                                <th>Telefonszám</th>
                                <th>Felhasználó jogosultság szint</th>
                                <th>Műveletek</th>
                            </tr>
                            </thead>
                            <tbody>
                            {users.map(user => (
                                <tr className="rows" key={user.userId}>
                                    <td>{user.name}</td>
                                    <td>{user.userName}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>{user.userRights.userRightsName}</td>
                                    <td>
                                        <button
                                            className="btn btn-primary"
                                            onClick={() => openModal(user)}
                                        >
                                            Módosítás
                                        </button>
                                        {user.userRights.userRightsName !== 'Recepcios' && user.userRights.userRightsName !== 'Szolgaltato' && (
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => confirmDeleteUser(user.userId, user.userName, user.name)}
                                            >
                                                Delete
                                            </button>
                                        )}
                                    </td>
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
            {/* Modal */}
            {selectedUser && (
                <UpdateUserModal
                    isOpen={isModalOpen}
                    onClose={closeModal}
                    userId={selectedUser.userId}
                    userName={selectedUser.userName}
                    initialData={selectedUser}
                    refreshUsers={fetchUsers}
                />
            )}
        </>
    );
}

export default UserFilterPage;
