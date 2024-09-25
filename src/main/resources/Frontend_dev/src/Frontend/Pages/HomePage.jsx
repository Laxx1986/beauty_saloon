import React from "react";

function HomePage() {
    return (
      <div>
          <img src={process.env.PUBLIC_URL + '/assets/saloon_photo.jpg'} className="img-fluid" alt="saloon_photo"/>
          <h1>Beauty Szépségszalon</h1>
          <span><h2>Műköröm Szolgáltatás</h2>
    <p><strong>Műköröm szolgáltatásunk</strong> a legújabb divatirányzatoknak megfelelően készült, széles választékban elérhető színek és minták közül lehet választani. Műköröm technikusaink ügyelnek arra, hogy a körömformák és a díszítések tökéletesen illeszkedjenek a vendég stílusához. A tartós és elegáns körmök mellett a műköröm építése és díszítése révén vendégeink kifejezhetik egyéni stílusukat.</p>

    <h2>Pedikűr Szolgáltatás</h2>
    <p>A <strong>pedikűr szolgáltatásunk</strong> szintén nagy népszerűségnek örvend, amely a lábak ápolására és felfrissítésére specializálódott. Pedikűrös szakembereink nemcsak a láb körmeit formázzák, hanem a bőrkeményedéseket és egyéb bőrproblémákat is kezelik. A kényeztető lábmasszázs és a hidratáló kezelések révén vendégeink nemcsak szép, hanem egészséges lábakkal távozhatnak.</p>

    <h2>Fodrászat</h2>
    <p>Fodrászatunkban a <strong>legújabb frizura trendek</strong> és technikák várják vendégeinket. Fodrászaink széleskörű tapasztalattal rendelkeznek, és személyre szabott tanácsokkal segítik a vendégeket a legmegfelelőbb frizura kiválasztásában. Legyen szó egyszerű vágásról, színezésről vagy különleges alkalmakra való frizuráról, a vendégek biztosak lehetnek abban, hogy a legjobb kezekben vannak.</p>

    <p>Szépségszalonunk nemcsak a testi szépségre, hanem a <strong>belső harmóniára</strong> is fókuszál. Minden kezelés során arra törekszünk, hogy vendégeink egy valódi feltöltődést és relaxációt éljenek át. <strong>Barátságos környezet, szakképzett személyzet</strong> és kiváló szolgáltatások várják Önt. Látogasson el hozzánk, és tapasztalja meg a szépség világát, ahol mindenki megtalálhatja a számára legideálisabb kezelést!</p>
          </span>
      </div>
    );
}

export default HomePage;