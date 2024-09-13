import React from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Dropdown } from 'react-bootstrap';


function AdminPage() {
    return (
        <>
            <h1>Admin tevékenységek</h1>
            <nav>
                <div className="container-fluid justify-content-center"  className="col-12">
                    <div id="navbarNav"  className="col-12">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <Dropdown>
                                    <Dropdown.Toggle variant="light" id="dropdown-basic">
                                        More
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu>
                                        <Dropdown.Item href="/userfilter">Regisztrált felhasználók</Dropdown.Item>
                                        <Dropdown.Item href="/serviceproviderfilter">Szolgáltatók listája</Dropdown.Item>
                                        <Dropdown.Item href="/servicelengthfilter">Szolgáltatás hossz</Dropdown.Item>
                                        <Dropdown.Item href="/servicefilter">Szolgáltatások</Dropdown.Item>
                                        <Dropdown.Item href="/openingtimefilter">Nyitvatartás</Dropdown.Item>
                                        <Dropdown.Item href="/bookingfilter">Foglalások</Dropdown.Item>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

        </>
    )
}

export default AdminPage;