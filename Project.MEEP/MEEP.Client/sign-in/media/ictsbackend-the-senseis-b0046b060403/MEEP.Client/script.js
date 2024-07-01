var token = localStorage.getItem('token')

function getEvents() {

    fetch('http://localhost:8090/evento/filter', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: token
      },
      body: JSON.stringify({}),
      
    })
      .then((response) => {
        if (response.ok) {
            return response.json(); 
        } else {
          throw new Error('Errore nella richiesta. Stato: ' + response.status);
        }
      })
      .then((data) => {
        //console.log('Dati della risposta:', data); // Stampare i dati ricevuti
        showEvents(data);
        getFotoData(data);
      })
      .catch((error) => {
        // Gestione degli errori
        console.error('Si è verificato un errore:', error);
      });
  }

  function showEvents(eventi) {
    var CONTAINER = document.getElementById("eventi");
    eventi.forEach(evento => {

        var DIVCOL = document.createElement("div");
        DIVCOL.setAttribute("class","col");
        var DIVSHADOW = document.createElement("div");
        DIVSHADOW.setAttribute("class","card shadow-sm");

        let imgPath="http://localhost:8090/evento/"+evento.codiceEvento+"/foto"
        var imgElement = document.createElement("img")
        imgElement.setAttribute("class", "bd-placeholder-img card-img-top");
        imgElement.setAttribute("width", "100%");
        imgElement.setAttribute("height", "225");
        imgElement.setAttribute("preserveAspectRatio","xMidYMid slice");
        imgElement.setAttribute("aria-label", "Placeholder: Thumbnail");
        let imgid="evento"+evento.codiceEvento.toString()
        imgElement.setAttribute("id",imgid)

        var SVGRECT = document.createElement("rect");
        SVGRECT.setAttribute("width","100%");
        SVGRECT.setAttribute("height","100%");
        SVGRECT.setAttribute("fill","#55595c");
        var SVGTEXT = document.createElement("text");
        SVGTEXT.setAttribute("fill","#eceeef");
        SVGTEXT.setAttribute("x","50%");
        SVGTEXT.setAttribute("y","50%");
        SVGTEXT.setAttribute("dy",".3em");
        SVGTEXT.innerText=evento.titolo;

        DIVSHADOW.appendChild(imgElement);
        DIVSHADOW.appendChild(SVGRECT);
        DIVSHADOW.appendChild(SVGTEXT);

        var DIVBODY = document.createElement("div");
        DIVBODY.setAttribute("class","card-body");
        var PDESC = document.createElement("p") 
        PDESC.setAttribute("class","card-text");
        PDESC.innerText=evento.descrizione;
        DIVBODY.appendChild(PDESC);
        var BODYBOTTOM = document.createElement("div");
        BODYBOTTOM.setAttribute("class","d-flex justify-content-between align-items-center");
        var BPARTECIPA = document.createElement("button");
        BPARTECIPA.setAttribute("class","btn btn-sm btn-outline-secondary");
        BPARTECIPA.setAttribute("type","button");
        BPARTECIPA.innerText="Partecipa";

        BODYBOTTOM.appendChild(BPARTECIPA);
        var SADMIN = document.createElement("small");
        SADMIN.setAttribute("class","text-body-secondary");
        SADMIN.innerText=evento.utenteAdmin.username;
        BODYBOTTOM.appendChild(SADMIN);
        DIVBODY.appendChild(BODYBOTTOM)

        DIVSHADOW.appendChild(DIVBODY);
        DIVCOL.appendChild(DIVSHADOW);
        CONTAINER.appendChild(DIVCOL);
        
    });
  }

  function getFotoData(eventi) { {

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
  getEvents();
  getFotoData();

  