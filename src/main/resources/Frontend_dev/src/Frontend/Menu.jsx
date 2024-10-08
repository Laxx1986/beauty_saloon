import { Link } from "react-router-dom";

function Menu({ login, userRights, openUpdateUserModal }) {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid justify-content-center">
                <div id="navbarNav">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Kezdőlap</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/services">Szolgáltatások</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/pricelist">Árlista</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/about">Rólunk</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/contact">Kapcsolat</Link>
                        </li>
                        {userRights && (userRights === 'Recepcios' || userRights === 'Szolgaltato') && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/admin">Admin</Link>
                            </li>
                        )}
                        {userRights && (userRights === 'Recepcios' || userRights === 'Szolgaltato') && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/statistics">Statisztika</Link>
                            </li>
                        )}
                        {userRights === 'User' && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/Bookings">Új foglalás</Link>
                            </li>
                        )}
                        {userRights === 'User' && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/BookingTablePageForUsers">Foglalások táblázat</Link>
                            </li>
                        )}

                        {userRights === 'User' && (
                            <li className="nav-item">
                                <button className="nav-link btn btn-link" onClick={openUpdateUserModal}>Felhasználói adat módosítása</button>
                            </li>
                        )}

                        {!login && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/registration">Regisztráció</Link>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default Menu;
