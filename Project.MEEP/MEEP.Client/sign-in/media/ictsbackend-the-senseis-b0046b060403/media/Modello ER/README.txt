- Evento: 
  Gli eventi hanno un codice evento univoco, un utente amministratore (utenteAdmin), un titolo, una descrizione, un'immagine di copertina (thumbnail), numero massimo di partecipanti,
  data e ora, numero massimo di accompagnatori che i partecipanti possono portare (maxAccompagnatori) e la possibilità di essere eventi pubblici (a cui tutti i dipendenti possono partecipare) oppure eventi privati (a cui bisogna essere autorizzati per partecipare); 
  Per gestire la distinzione tra evento pubblico e privato si utilizza il campo booleano "isPrivato"; Se l'evento è privato, vale true altrimenti è false.

- Tipologia: 
  Indica la tipologia (o tags) degli eventi. Caratterizzato da un id e dalla denominazione.

- EventoTipologia:
  Tabella per gestire la relazione N:N tra Evento e Tipologia.

- Invito:
  Utilizzato per gestire gli inviti agli eventi privati; Ha due FK: il codice dell'evento e il codice dell'utente invitato. 
  Inoltre gli inviti hanno uno stato (gestito dal campo booleano "isAccettato", di valore true se l'utente invitato ha accettato l'invito all'evento) e la data e ora dell'invio.

- Utente:  
  Questa tabella contiene tutti i dati degli utenti (ossia i dipendenti che possono registrarsi tramite matricola, fare login e creare/partecipare ad eventi).
  Questi dati comprendono: nome, cognome, matricola aziendale (univoca), email, password, telefono, data di nascita, foto profilo e una biografia (descrizione del profilo)

- Indirizzo:
  L'indirizzo rappresenta il luogo degli eventi ed è unico per ogni utente (ecco perché la chiave primaria è la FK della matricola utente). 
  Caratterizzato da via (strada, ecc) , cap, comune e provincia.

- Partecipazione:
  Questa tabella ci consente di tener conto delle partecipazioni agli eventi. la chiave primaria é rappresentata dal codice del QR code che verrà invitato una volta confermata la partecipazione di un utente .
  Inoltre, la tabella è caratterizzata da 2 FK (codice evento e matricola dipendente) e dal numero di accompagnatori (ossia il numero di familiari/conoscenti che il dipendente vuole portare all'evento). 

- RichiestaAmicizia:
  Questa tabella ci permette di gestire le richieste di amicizia. è caratterizzata da un id, data e ora dell'invio, 2 FK che richiamano l'utente (utente1 rappresenta l'utente che INVIA la richiesta mentre l'utente2 la RICEVE)e 
  infine il campo "isAccettato" ossia un valore booleano che ci consente di gestire lo stato della richiesta (null se non è stata né accettata né rifiutata, true se è stata accettata, false se è stata rifiutata).

- Amicizia:
  Come suggerisce il nome, è la tabella che ci consente di gestire la lista degli amici delle utenze. 