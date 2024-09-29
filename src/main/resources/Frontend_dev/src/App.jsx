import React, { useEffect, useState } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Link, Route, Routes, useLocation, useNavigate} from 'react-router-dom';
import axiosInstance from './AxiosInterceptor';
import Menu from './Frontend/Menu';
import HomePage from "./Frontend/Pages/HomePage";
import ServicePage from "./Frontend/Pages/ServicePage";
import PriceListPage from "./Frontend/Pages/PriceListPage";
import AboutPage from "./Frontend/Pages/AboutPage";
import ContactPage from "./Frontend/Pages/ContactPage";
import AdminPage from "./Frontend/Pages/AdminPage";
import RegistrationPage from "./Frontend/Pages/RegistrationPage";
import UserFilterPage from "./Frontend/Pages/AdminFilters/UserFilterPage";
import ServiceProviderFilterPage from "./Frontend/Pages/AdminFilters/ServiceProviderFilterPage";
import ServiceLengthFilterPage from "./Frontend/Pages/AdminFilters/ServiceLengthFilterPage";
import BookingTableFilter from "./Frontend/Pages/AdminFilters/BookingTableFilter";
import ServiceFilterPage from "./Frontend/Pages/AdminFilters/ServiceFilterPage";
import OpeningTimeFilterPage from "./Frontend/Pages/AdminFilters/OpeningTimeFilterPage";
import MyCalendar from "./Frontend/Pages/MyCalendar";
import LoginPage from "./Frontend/Pages/LoginPage";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState('');
  const [userRights, setUserRights] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const storedIsLoggedIn = localStorage.getItem('isLoggedIn');
    const storedUserName = localStorage.getItem('userName');
    const storedUserRights = localStorage.getItem('userRights');

    if (storedIsLoggedIn === 'true' && storedUserName) {
      setIsLoggedIn(true);
      setUserName(storedUserName);

      if (storedUserRights) {
        try {
          const parsedRights = JSON.parse(storedUserRights); // Parse only if it exists
          setUserRights(parsedRights);
        } catch (error) {
          console.error("Error parsing userRights from localStorage:", error);
          setUserRights(null); // Handle parsing error
        }
      }
    }
  }, [location]);

  const handleLogout = async () => {
    setIsLoggedIn(false);
    setUserName('');
    setUserRights(null);
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    localStorage.removeItem('userRights');
    navigate('/login');
  };

  return (
      <>
        <div className="row" id="needbackground">
          <div className="col-12">
            <h2 className="saloontitle">General Beauty Saloon Nails&Hair</h2>
            {!isLoggedIn ? (
                <div className="col-12" id="loginbutton">
                  <Link to="/login">
                    <button type="button">Bejelentkezés</button>
                  </Link>
                </div>
            ) : (
                <div className="col-12" id="logoutbutton">
                  <p>Üdvözlünk, {userName}!</p>
                  <button type="button" onClick={handleLogout}>Kijelentkezés</button>
                </div>
            )}
          </div>
        </div>
        <div className="row" id="needbackground">
          <div className="col-12">
            <Menu login={isLoggedIn} userRights={userRights} />
          </div>
        </div>
        <div className="row" id="needbackground">
          <div className="col-12">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/services" element={<ServicePage />} />
              <Route path="/pricelist" element={<PriceListPage />} />
              <Route path="/about" element={<AboutPage />} />
              <Route path="/contact" element={<ContactPage />} />
              {userRights && (userRights === 'Recepcios' || userRights === 'Szolgaltato') && (
                  <Route path="/admin" element={<AdminPage />} />
              )}
              {userRights === 'User' && (
                  <Route path="/Bookings" element={<MyCalendar />} />
              )}
              {!isLoggedIn && <Route path="/registration" element={<RegistrationPage />} />}
              {userRights && (userRights === 'Recepcios' || userRights === 'Szolgaltato') && (
                  <>
                    <Route path="/userfilter" element={<UserFilterPage />} />
                    <Route path="/serviceproviderfilter" element={<ServiceProviderFilterPage />} />
                    <Route path="/servicelengthfilter" element={<ServiceLengthFilterPage />} />
                    <Route path="/servicefilter" element={<ServiceFilterPage />} />
                    <Route path="/openingtimefilter" element={<OpeningTimeFilterPage />} />
                    <Route path="/bookingfilter" element={<MyCalendar key="booking-calendar" />} />
                    <Route path="/bookingtablefilter" element={<BookingTableFilter/>} />
                  </>
              )}
              <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} setUserName={setUserName} setUserRights={setUserRights} />} />
            </Routes>
          </div>
        </div>
      </>
  );
}

export default App;
