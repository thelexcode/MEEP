var token = localStorage.getItem('token')
var matricola = localStorage.getItem('matricola')

//getFilters -> funzione per ricevere dal server gli eventi filtrati
async function getFilters(){
  let DIVTIPOLOGIA = document.getElementById("listTipologie");
  const checkboxes = DIVTIPOLOGIA.querySelectorAll('input[type="checkbox"]');
  const tipologie = [];
  checkboxes.forEach(c =>{
    if(c.checked==true)
      tipologie.push(c.value);    //ottieni gli id delle tipologie selezionate
  })
  
  var dataEvento=document.getElementById("data-evento").value;
  var oraEvento=document.getElementById("ora-evento").value;
  var partecipanti=document.getElementById("partecipanti").value;
  var adminIncluded=document.getElementById("eventiAdmin").checked;
  var filters ={}

  if(dataEvento!="")
     filters.dataEvento=dataEvento;

  if(oraEvento!="")
    filters.oraEvento=oraEvento+":00";
  
  if(partecipanti!="")
    filters.maxPartecipanti=partecipanti;

  if(tipologie!=[])
    filters.idTipologia=tipologie;

  filters.includeAdmin=adminIncluded

  await getEvents(filters)
}

//getTipologie -> funzione per recuperare i dati delle tipologie evento
function getTipologie(){
  fetch('http://localhost:8090/tipologia/all', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: token
      }
    })
      .then((response) => {
        if (response.ok) {
            return response.json(); 
        } else {
          throw new Error('Errore nella richiesta. Stato: ' + response.status);
        }
      })
      .then((data) => {
        var DIVTIPOLOGIA = document.getElementById("listTipologie");
        data.forEach(tipologia => {
          var LI = document.createElement("li")
          var CHECKBOX  = document.createElement("input")
          CHECKBOX.setAttribute("type","checkbox");
          CHECKBOX.setAttribute("name","Tipologia[]")
          CHECKBOX.setAttribute("value",tipologia.idTipologia);
          LI.appendChild(CHECKBOX);
          LI.append(tipologia.denominazione);
          DIVTIPOLOGIA.appendChild(LI);
        });

      })
      .catch((error) => {
        // Gestione degli errori
        console.error('Si è verificato un errore:', error);
      });
}

//getEvents -> funzione per recuperare i dati degli eventi disponibili
async function getEvents(filters={}) {

    await fetch('http://localhost:8090/evento/filter', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: token
      },
      body: JSON.stringify(filters),
      
    })
      .then((response) => {
        if (response.ok) {
            return response.json(); 
        } else {
          throw new Error('Errore nella richiesta. Stato: ' + response.status);
        }
      })
      .then((data) => {
        showEvents(data);
        getFotoData(data);
      })
      .catch((error) => {
        console.error('Si è verificato un errore:', error);
      });
  }

  //showEvents -> funzione che permette, ricevuti i dati degli eventi dal server, di visualizzarli nella pagina HTML 
  async function showEvents(eventi) {
    var CONTAINER = document.getElementById("eventi");
    CONTAINER.innerHTML="";
    var INNERHTML=[];

    eventi.forEach( (evento) => {
        if(evento.utenteAdmin.matricola==matricola){      //se l'utente è amministratore dell'evento...
          var button=`<button class="btn btn-sm btn-outline-secondary" type="button">Modifica</button>`
        }else{
          var button=`<button class="btn btn-sm btn-outline-secondary" onclick="window.location.href='contatti_accompagnatori.html?evento=${evento.codiceEvento}'">Partecipa</button>`
        }
        let htmlEvento=`<div class="col">
        <div class="card shadow-sm">
          <img id="evento${evento.codiceEvento}" class="bd-placeholder-img card-img-top" width="100%" height="225" preserveAspectRatio="xMidYMid slice" aria-label="Placeholder: Thumbnail" />
            <title>Placeholder</title>
          <div class="card-body">
            <div class="col">
              <h5>${evento.titolo}</h5>
              <p class="card-text">${evento.descrizione}</p>
              <div class="row align-items-start">
                <div class="col-auto">
                  <img src="icons/location-pin.png" alt="" class="icon">
                </div>
                <div class="col">
                  <p name="indirizzo-${evento.codiceEvento}" id="indirizzo-${evento.codiceEvento}" class="info"></p>
                </div>
              </div>
              <div class="row align-items-start">
                <div class="col-auto">
                  <img src="icons/calendar.png" alt="" class="icon">
                </div>
                <div class="col">
                  <p class="info">Data: ${evento.data}</p>
                </div>
              </div>
              <div class="row align-items-start">
                <div class="col-auto">
                  <img src="icons/clock.png" alt="" class="icon">
                </div>
                <div class="col">
                  <p class="info">Ora: ${evento.oraInizio} - ${evento.oraFine}</p>
                </div>
              </div>
              <div name="maxPartecipanti" class="row align-items-start">
                <div class="col-auto">
                  <img src="icons/teamwork.png" alt="" class="icon">
                </div>
                <div class="col">
                  <p class="info">Max partecipanti: ${evento.maxPartecipanti}</p>
                </div></div>
                <div class="d-flex justify-content-between align-items-center">
                `+button+`
                <small class="text-body-secondary">${evento.utenteAdmin.matricola}</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>`
      INNERHTML.push(htmlEvento);
    });
    INNERHTML.forEach(i => {
      CONTAINER.innerHTML=CONTAINER.innerHTML+i
    });
  }


  //getFotoData -> recupera le foto di copertina degli eventi dal database
  async function getFotoData(eventi) { {

    eventi.forEach(evento => {
      var imgPath = 'http://localhost:8090/evento/' + evento.codiceEvento+"/foto"; 

      fetch(imgPath, {
        headers: {
          'Access-Control-Allow-Origin': '*',
          Authorization: token,
        },
      })
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
}

  }

  async function getLocation() { 
    let ELINDIRIZZI = document.querySelectorAll('[name^="indirizzo"]');
    ELINDIRIZZI.forEach( elIndirizzo => {
        let codiceEvento = elIndirizzo.getAttribute("name").split("-")[1]
        var path = "http://localhost:8090/evento/"+codiceEvento+"/indirizzo";
  
      fetch(path, {
        headers: {
            'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          Authorization: token,
        },
      })
        .then(async (response) =>{
          if (response.ok) {
            let data = await response.json();
            let i = data.via + ", " + data.comune;
            elIndirizzo.innerHTML="Luogo: "+i;
          } else {
            throw new Error('Errore nella richiesta. Stato: ' + response.status);
          }
        })
        .catch((error) => {
          console.log("Errore durante il recupero dell'immagine:", error);
        });
    });
}

async function loadIndirizzo() {
  await getFilters().then(()=>{
      getLocation()  
  });
}
  
  
