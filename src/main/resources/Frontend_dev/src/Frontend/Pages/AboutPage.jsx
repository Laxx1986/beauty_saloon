import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

function AboutPage() {
    return (
        <div className="container mt-5">
            <h1 className="text-center mb-4">Szolgáltatások</h1>
            <div className="row">

                {/* Fodrász - Kiss Dominika */}
                <div className="col-md-6 mb-4">
                    <h2>Kiss Dominika</h2>
                    <img
                        src="/assets/hair_dresser.jpg"
                        alt="Kiss Dominika"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        Kiss Dominika tapasztalt fodrász, aki férfi és női hajvágásban egyaránt jártas. Széleskörű tudásának
                        köszönhetően a legújabb trendek szerint dolgozik, figyelembe véve a kliensek egyéni igényeit. Dominika
                        szakértelme kiterjed a hajfestés különböző technikáira is, beleértve az ombre és balayage stílusokat.
                        Minden vendégét egyedülálló figyelemmel és gondossággal kezeli, hogy biztosítsa a tökéletes megjelenést.
                        Az általa alkalmazott professzionális termékek garantálják a haj egészségének megőrzését. Dominika
                        hivatása iránti szenvedélye és elkötelezettsége miatt mindenki számára kiváló szolgáltatást nyújt.
                        Legyen szó egy egyszerű frizuráról vagy egy merész stílusváltásról, Dominika a legjobbat hozza ki a hajadból.
                    </p>
                </div>

                {/* Fodrász - Kara Gabriella */}
                <div className="col-md-6 mb-4">
                    <h2>Kara Gabriella</h2>
                    <img
                        src="/assets/hair_dresser.jpg"
                        alt="Kara Gabriella"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        Kara Gabriella egy olyan fodrász, aki a speciális kezelésekre specializálódott, beleértve a hajkezelések
                        és helyreállítási technikák széles skáláját. Gabriella célja, hogy minden vendége számára a legjobb
                        élményt nyújtsa, figyelembe véve az egyéni hajtípust és a szükségleteket. Szakmai háttere lehetővé
                        teszi számára, hogy különféle hajproblémákat kezeljen, legyen az szárazság, töredezett haj vagy éppen
                        hajhullás. Gabriella a legújabb termékeket és technikákat használja, hogy vendégei haját egészségesebbé
                        és ragyogóbbá tegye. Az ő tudása és tapasztalata segít abban, hogy a vendégek önbizalma növekedjen,
                        hiszen a hajvágás és a hajápolás terén minden részletre figyel. Különösen figyelmet fordít a vendégek
                        igényeire, így mindenki számára a legmegfelelőbb megoldást kínálja.
                    </p>
                </div>
            </div>

            <div className="row">
                {/* Műkörmös - Szendrey Dóra */}
                <div className="col-md-6 mb-4">
                    <h2>Szendrey Dóra</h2>
                    <img
                        src="/assets/manicure.jpg"
                        alt="Szendrey Dóra"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        Szendrey Dóra műkörmös és pedikűrös, aki a szép körmök iránti szenvedéllyel végzi munkáját. Tapasztalata
                        és szakmai tudása révén garantálja a minőségi szolgáltatást. Dóra különféle műköröm technikákban
                        jártas, beleértve a gél és akril körmök készítését, valamint a különböző díszítési lehetőségeket.
                        Minden körömdíszítést egyedileg tervez, figyelembe véve a vendégek egyéni stílusát és ízlését. A
                        legújabb trendek és technikák alkalmazásával Dóra mindig a legjobb eredményt nyújtja, így a kezeid
                        mindig csodálatosan néznek ki. Emellett figyelmet fordít a körmök egészségére is, hogy a vendégek
                        mindig elégedettek legyenek. A vele való együttműködés során garantált a kellemes légkör és a
                        maximális elégedettség.
                    </p>
                </div>

                {/* Kozmetikus - Wass Dóra */}
                <div className="col-md-6 mb-4">
                    <h2>Wass Dóra</h2>
                    <img
                        src="/assets/cosmetic.jpg"
                        alt="Wass Dóra"
                        className="img-fluid rounded border border-light"
                        style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}
                    />
                    <p className="mt-3">
                        Wass Dóra professzionális kozmetikus, aki a legmagasabb minőségű anyagokkal dolgozik a bőrápolás
                        terén. Szakmai tapasztalata lehetővé teszi számára, hogy a legjobb kozmetikai kezeléseket nyújtsa,
                        beleértve a hidratáló arckezeléseket, bőrtisztítást és kozmetikai tetoválásokat. Dóra mindig figyelembe
                        veszi a vendégek bőrtípusát és igényeit, így minden kezelés személyre szabott és hatékony. Az általa
                        alkalmazott professzionális termékek garantálják a legjobb eredményeket, így a vendégek mindig
                        elégedettek lehetnek a bőrük állapotával. Dóra elkötelezett amellett, hogy a vendégek ne csak szépnek,
                        hanem magabiztosnak is érezzék magukat. Kellemes és nyugodt környezetet biztosít, ahol a pihenés és
                        a regenerálódás egyaránt biztosított, így a kozmetikai kezelések valódi élvezetté válnak.
                    </p>
                </div>
            </div>
        </div>
    );
}

export default AboutPage;
