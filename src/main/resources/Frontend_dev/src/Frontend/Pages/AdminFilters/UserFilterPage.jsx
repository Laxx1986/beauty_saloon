import { Link } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ConfirmModal from './ConfirmModal';
import './Tables.css';

function UserFilterPage() {
    const [users, setUsers] = useState([]);
    const [confirmDelete, setConfirmDelete] = useState({ show: false, userId: null, userName: '', name: '' });
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        axios.get('http://localhost:8080/api/users/all-users', {
            auth: {
                username: 'admin',
                password: 'almafa'
            }
        })
            .then(response => {
                setUsers(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    const handleDelete = (userId) => {
        axios.delete(`http://localhost:8080/api/users/${userId}`, {
            auth: {
                username: 'admin',
                password: 'almafa'
            }
        })
            .then(response => {
                setUsers(users.filter(user => user.userId !== userId));
                setFeedback('A felhasználó törölve lett');
            })
            .catch(error => {
                console.error('Error deleting user:', error);
            });
    };

    const confirmDeleteUser = (userId, userName, name) => {
        setConfirmDelete({ show: true, userId, userName, name });
    };

    const cancelDelete = () => {
        setConfirmDelete({ show: false, userId: null, userName: '', name: '' });
    };

    const proceedDelete = () => {
        handleDelete(confirmDelete.userId);
        setConfirmDelete({ show: false, userId: null, userName: '', name: '' });
    };

    return (
        <>
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
                                <th>Felhasználó azonosító</th>
                                <th>Felhasználó név</th>
                                <th>Email</th>
                                <th>Telefonszám</th>
                                <th>Jelszó</th>
                                <th>Felhasználó jogosultság szint</th>
                                <th>Műveletek</th>
                            </tr>
                            </thead>
                            <tbody>
                            {users.map(user => (
                                <tr className="rows" key={user.userId}>
                                    <td>{user.userId}</td>
                                    <td>{user.userName}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>{user.password}</td>
                                    <td>{user.userRights.userRightsName}</td>
                                    <td>
                                        <Link to={`/edit-user/${user.userId}`}>
                                            <button className="btn btn-primary">Update</button>
                                        </Link>
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

            <ConfirmModal
                show={confirmDelete.show}
                message={`Biztosan törölni akarod a ${confirmDelete.userName}, ${confirmDelete.name} felhasználót?`}
                onConfirm={proceedDelete}
                onCancel={cancelDelete}
            />
        </>
    );
}

export default UserFilterPage;
