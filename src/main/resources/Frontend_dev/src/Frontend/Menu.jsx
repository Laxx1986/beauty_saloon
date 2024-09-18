import { Link } from "react-router-dom";
import { useEffect } from "react";

function Menu({ login, userRights }) {
    useEffect(() => {
    }, [userRights]);

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid justify-content-center">
                <div id="navbarNav">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Home page</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/services">Services</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/pricelist">Price List</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/about">About us</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/contact">Contact us</Link>
                        </li>
                        {userRights && (userRights.userRightsName === 'Recepcios' || userRights.userRightsName === 'Szolgaltato') && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/admin">Admin</Link>
                            </li>
                        )}
                        {userRights && userRights.userRightsName === 'User' && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/Bookings">Bookings</Link>
                            </li>
                        )}
                        {!login && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/registration">Register</Link>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default Menu;
