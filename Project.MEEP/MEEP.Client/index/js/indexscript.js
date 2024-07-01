var token = localStorage.getItem('token')

function getEvents() {

    fetch('http://localhost:8090/home/eventi', {
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
            showRecentEvents(data)
        
      })
      .catch((error) => {
        console.error('Si è verificato un errore:', error);
      });
  }

  async function showRecentEvents(eventi){
    var ITEMHTML=[]
    for (let i = 0; i < eventi.length; i++) {
      if(i>8)
        break;
        try {
                var indirizzo= await getLocation(eventi[i])
        } catch (error) {
                console.error('Si è verificato un errore:', error);
        }
      var row = `<div class="row">`;
      let evento = `
        <div class="col-lg-12">
        <div class="list-item">
        <div class="left-image">
          <img name="${'evento'+eventi[i].codiceEvento}" src="index/assets/images/listing-05.jpg" class="eventimg" alt="">
        </div>
        <div class="right-content align-self-center">
            <h4>${eventi[i].titolo}</h4>
          <h6>by: ${eventi[i].utenteAdmin.username}</h6>
          <span class="price">
            <div class="icon"><img src="icons/location-pin.png"  alt="" ></div><span name="indirizzo">${indirizzo}
          </span>
          <span class="price">
          <div class="icon"><img src="icons/edit-info.png"  alt="" ></div><span name="indirizzo">${eventi[i].descrizione}</span>
          

        </span>
          
          <ul class="info">
          <li>
  <img src="icons/calendar.png" alt="" style="width: 16px; height: 16px; transform: translateX(7px);">
  ${eventi[i].data}
</li>
<li>
  <img src="icons/clock.png" alt="" style="width: 16px; height: 16px; transform: translateX(7px);">
  ${eventi[i].oraInizio} - ${eventi[i].oraFine}
</li>

        
            

           
          </ul>
          <a class="btn btn-sm btn-primary m-2 text-white" href="contatti_accompagnatori.html" role="button" rel="nofollow" target="_blank" style="transform: translate(500px, -10px);">Partecipa</a>




         
          
           
          

          
        </div>
        </div>
        </div>
      `;
      var rowEnd = `</div>`;
      ITEMHTML.push(evento);
    }
    console.log(ITEMHTML)
    let ITEM1= document.getElementsByName("item1")
    let items= row+ITEMHTML[0]+ITEMHTML[1]+ITEMHTML[2]+rowEnd
    ITEM1.forEach(it =>{
        it.innerHTML=items;
    })
    ITEM1.innerHTML=items;
    let ITEM2= document.getElementsByName("item2")
    let items2=row+ITEMHTML[3]+ITEMHTML[4]+ITEMHTML[5]+rowEnd;
    ITEM2.forEach(it =>{
        it.innerHTML=items2;
    })
    let ITEM3= document.getElementsByName("item3")
     let items3 = row+ITEMHTML[6]+ITEMHTML[7]+ITEMHTML[8]+rowEnd;
     ITEM3.forEach(it =>{
        it.innerHTML=items3;
    })
    getFotoData(eventi)
  }

  async function getLocation(evento) {
    let path = "http://localhost:8090/user/info/"+evento.utenteAdmin.matricola
    const response = await fetch(path, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token
        },
      });
    
      if (response.ok) {
        const data = await response.json();
        return data.via + ", " + data.comune;
      } else {
        throw new Error('Errore nella richiesta. Stato: ' + response.status);
      }
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
          let imgname="evento"+evento.codiceEvento.toString()
          var eventoimg = document.getElementsByName(imgname)
          eventoimg.forEach(i => {
            i.setAttribute("src",imageUrl); 
          });
        })
        .catch((error) => {
          console.log("Errore durante il recupero dell'immagine:", error);
        });
    });
  }
  
}

  getEvents();