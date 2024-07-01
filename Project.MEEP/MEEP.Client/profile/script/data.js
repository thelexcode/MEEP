var token = localStorage.getItem('token')
var matricola = localStorage.getItem('matricola')

function getUserData() {
    let path = 'http://localhost:8090/user/info/'+matricola;
    fetch(path, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: token
      },
      
    })
      .then((response) => {
        if (response.ok) {
            return response.json(); 
        } else {
          throw new Error('Errore nella richiesta. Stato: ' + response.status);
        }
      })
      .then((data) => {
            showUserData(data)
            getFotoProfilo()
            getOrganizedEvents();
      })
      .catch((error) => {
        console.error('Si è verificato un errore:', error);
      });
  }

  function showUserData(utente) {
    let ciao = document.getElementById("ciao");
    ciao.innerHTML="Ciao "+utente.nome;

    document.getElementById("pmatricola").innerHTML=utente.matricola;
    document.getElementById("pnome").innerHTML=utente.nome;
    document.getElementById("pcognome").innerHTML=utente.cognome;
    var dataNascita = utente.dataNascita.split("T")[0]
    document.getElementById("pdataNascita").innerHTML=dataNascita;
    document.getElementById("pemail").innerHTML=utente.email;
    document.getElementById("pusername").innerHTML=utente.username;
    document.getElementById("ptelefono").innerHTML=utente.telefono;

    document.getElementById("pprovincia").innerHTML=utente.provincia;
    document.getElementById("pcomune").innerHTML=utente.comune;
    document.getElementById("pindirizzo").innerHTML=utente.via;
    document.getElementById("pcap").innerHTML=utente.cap;
    document.getElementById("pbiografia").innerHTML=utente.biografia;

  }

  function getOrganizedEvents() {
    let path = 'http://localhost:8090/user/eventi/'+matricola;
    fetch(path, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: token
      },
      
    })
      .then((response) => {
        if (response.ok) {
            return response.json(); 
        } else {
          throw new Error('Errore nella richiesta. Stato: ' + response.status);
        }
      })
      .then((data) => {
            let DIVEVENTI= document.getElementById("eventi");
            let eventihtml=[];
            DIVEVENTI.innerHTML = "";
            data.forEach(evento => {
                let e=` <div class="col-md-4">
                <div class="card shadow-sm">
                <img id="evento${evento.codiceEvento}" class="bd-placeholder-img card-img-top" width="100%" height="225"
                src="" 
                preserveAspectRatio="xMidYMid slice" focusable="false"/>

                <div class="card-body">
                <div class="col">
                <h3 class="titcard">${evento.titolo}</h3>
                <p class="card-text">${evento.descrizione}</p>
                <div class="row align-items-start">
                    <div class="col-auto">
                        <img src="profile/img/calendar.png" alt="" class="icon">
                    </div>
                    <div class="col">
                        <p class="info">Data: ${evento.data}</p>
                    </div>
                </div>
                <div class="row align-items-start">
                    <div class="col-auto">
                        <img src="profile/img/clock.png" alt="" class="icon">
                    </div>
                    <div class="col">
                        <p class="info">Ora: ${evento.oraInizio} - ${evento.oraFine}</p>
                    </div>
                </div>
                <div class="row align-items-start">
                    <div class="col-auto">
                        <img src="profile/img/teamwork.png" alt="" class="icon">
                    </div>
                    <div class="col">
                        <p class="info">Max Partecipanti: ${evento.maxPartecipanti}</p>
                    </div>
                </div>
                </div>
                </div>
                </div>
                </div>`;
                eventihtml.push(e);
            });
            eventihtml.forEach(e =>{
                DIVEVENTI.innerHTML = DIVEVENTI.innerHTML+e;
            });
            return data;
      })
      .then(data =>{
            getFotoData(data);
      })
      .catch((error) => {
        console.error('Si è verificato un errore:', error);
      });
  }


function getFotoData(eventi) { {

    eventi.forEach(evento => {
    var imgPath = 'http://localhost:8090/evento/' + evento.codiceEvento+"/foto"; 
          
    fetch(imgPath, {
        headers: {
            'Access-Control-Allow-Origin': '*',
            Authorization: token,
        },})
        .then((response) => response.blob())
        .then((blob) => {
            var imageUrl = URL.createObjectURL(blob);
            let imgid="evento"+evento.codiceEvento.toString()
            var eventoimg = document.getElementById(imgid)
            eventoimg.setAttribute("src",imageUrl); 
        })
        .catch((error) => {
            console.log("Errore durante il recupero dell'immagine:", error);
        });
    });
}}

function getFotoProfilo() { {

    var imgPath = 'http://localhost:8090/user/' + matricola+"/foto"; 
          
    fetch(imgPath, {
        headers: {
            'Access-Control-Allow-Origin': '*',
            Authorization: token,
        },})
        .then((response) => response.blob())
        .then((blob) => {
            let imgURL = URL.createObjectURL(blob);
            var propic = document.getElementById("profile-image")
            propic.setAttribute("src",imgURL); 
        })
        .catch((error) => {
            console.log("Errore durante il recupero dell'immagine:", error);
        });
    }}

  getUserData();