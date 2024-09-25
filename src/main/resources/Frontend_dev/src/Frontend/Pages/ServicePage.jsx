import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

function ServicePage() {
    return (
        <div className="container mt-5">
            <h1 className="text-center mb-4">Szolgáltatások</h1>
            <div className="row">

                {/* Fodrászat */}
                <div className="col-md-6 mb-4">
                    <h2>Fodrászat</h2>
                    <img
                        src="/assets/hair_dresser.jpg"
                        alt="Fodrászat"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        A fodrászatban szakszerű hajvágási és hajformázási szolgáltatásokat kínálunk, amelyek célja a stílusos megjelenés elérése.
                        Fodrászaink tapasztalt szakemberek, akik segítenek megtalálni a hozzád legjobban illő frizurát, legyen szó akár klasszikus,
                        modern vagy extravagáns megoldásokról. A szolgáltatások között megtalálhatók a hajfestések, melírok, valamint a
                        különböző hajkezelések is, amelyek egészségesebbé és ragyogóbbá varázsolják a hajad.
                    </p>
                </div>

                {/* Műköröm */}
                <div className="col-md-6 mb-4">
                    <h2>Műköröm</h2>
                    <img
                        src="/assets/manicure.jpg"
                        alt="Műköröm"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        A műköröm szolgáltatásaink magukban foglalják a különféle műköröm technikák alkalmazását, beleértve a gél és akril körmök készítését.
                        Műköröm stylistjaink kreatív és egyedi megoldásokat kínálnak, hogy a kezeid mindig tökéletesen nézzenek ki.
                        Minden körömdíszítés a legújabb trendeknek megfelelően készül, legyen szó egyszerű, elegáns vagy extravagáns megoldásokról.
                    </p>
                </div>
            </div>

            <div className="row">
                {/* Pedikűr */}
                <div className="col-md-6 mb-4">
                    <h2>Pedikűr</h2>
                    <img
                        src="/assets/pedicure.jpg"
                        alt="Pedikűr"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        A pedikűr szolgáltatásaink során nemcsak a lábak esztétikáját javítjuk, hanem a láb egészségét is figyelembe vesszük.
                        Kínálunk hagyományos pedikűrt, valamint spa pedikűr kezeléseket is, amelyek frissítő hatással bírnak.
                        A kezelések során figyelmet fordítunk a bőr ápolására, a körmök formázására és a relaxációra is, hogy igazán élvezhesd a folyamatot.
                    </p>
                </div>

                {/* Kozmetikai Kezelések */}
                <div className="col-md-6 mb-4">
                    <h2>Kozmetikai Kezelések</h2>
                    <img
                        src="/assets/cosmetic.jpg"
                        alt="Kozmetikai Kezelések"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        A kozmetikai kezelések célja a bőr egészségének és szépségének megőrzése. Szolgáltatásaink közé tartozik a
                        hidratáló arckezelés, bőrtisztítás, anti-aging kezelések és sminktanácsadás. Kozmetikusaink személyre szabott
                        kezelésekkel várnak, hogy te is ragyogj! Külön figyelmet fordítunk a bőröd típusára, hogy a lehető legjobb
                        eredményeket érhessük el.
                    </p>
                </div>
            </div>
        </div>
    );
}

export default ServicePage;
