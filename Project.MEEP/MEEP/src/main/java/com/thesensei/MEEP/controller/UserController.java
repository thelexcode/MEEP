package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.request.EditUserRequest;
import com.thesensei.MEEP.request.IndirizzoRequest;
import com.thesensei.MEEP.request.LoginRequest;
import com.thesensei.MEEP.request.SignupRequest;
import com.thesensei.MEEP.service.IndirizzoService;
import com.thesensei.MEEP.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final IndirizzoService indirizzoService;

    public UserController(UserService userService, IndirizzoService indirizzoService) {
        this.userService = userService;
        this.indirizzoService = indirizzoService;
    }

    /**
     * signup() -> permette di completare la registrazione dell'utente (dopo aver effettuato il primo login) e invia all'utente una email di conferma
     * @param request ( newPassword, checkPassword, username, biografia)
     * @param httpRequest
     * @return response
     * @throws MessagingException
     * @throws IOException
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request, HttpServletRequest httpRequest) throws MessagingException, IOException {
        return userService.signup(request, httpRequest.getAttribute("matricola").toString(), httpRequest.getAttribute("email").toString());
    }

    /**
     * login() -> Date le corrette credenziali, permette di effettuare il login e restituisce un JWT
     * @param request (matricola, password)
     * @return response (token) , da utilizzare per l'autenticazione
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request){
        return userService.login(request);
    }

    /**
     * editProfilePicture() -> permette di sostituire la foto profilo dell'utente loggato con un nuovo file (che verrà caricato e salvato sul server)
     * @param file (nuovo file della foto profilo)
     * @param httpRequest (per ottenere la matricola dell'utente loggato dal token)
     * @return response
     */
    @PostMapping("/edit-foto")
    public ResponseEntity<?> editProfilePicture(@RequestParam MultipartFile file, HttpServletRequest httpRequest){
        return userService.editProfilePicture(file,httpRequest.getAttribute("matricola").toString());
    }

    /**
     * getProfilePicture() -> dato la matricola di un utente, ne restituisce la foto profilo
     * @param matricola
     * @return response (byte[] dell'immagine di profilo salvata sul server)
     * @throws IOException
     */
    @GetMapping("/{matricola}/foto")
    public ResponseEntity<?> getProfilePicture(@PathVariable String matricola) throws IOException {
        return userService.getProfilePicture(matricola);
    }

    /**
     * editUserData() -> permette di modificare i dati dell'utente autenticato
     * @param request (newPassword, checkPassword, username, email, telefono, biografia)
     * @param httpRequest
     * @return respone (restituisce l'oggetto User con i dati modificati)
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editUserData(@RequestBody @Valid EditUserRequest request, HttpServletRequest httpRequest){
        return userService.editUserData(request, httpRequest.getAttribute("matricola").toString());
    }

    /**
     * editIndirizzo() -> permette di modificare i dati dell'indirizzo dell'utente autenticato
     * @param request (via, cap, comune, provincia)
     * @param httpRequest
     * @return response (restituisce l'oggetto Indirizzo con i dati modificati)
     */
    @PutMapping("/indirizzo/edit")
    public ResponseEntity<?> editIndirizzo(@RequestBody @Valid IndirizzoRequest request, HttpServletRequest httpRequest){
        return indirizzoService.editIndirizzo(request,httpRequest.getAttribute("matricola").toString());
    }

    /**
     * getInfo() -> data la matricola di un utente, ne restituisce tutti i dati
     * @param matricola
     * @return response ( restituisce l'oggetto UserResponse con tutti i dati dell'utente e del suo indirizzo)
     */
    @GetMapping("/info/{matricola}")
    public ResponseEntity<?> getInfo(@PathVariable String matricola) {
        return userService.infoUtente(matricola);
    }

    /**
     * getAllUserEventi() -> permette di ottenere una lista di tutti gli eventi organizzati dall'utente autenticato
     * @param matricola
     * @return response (restituisce un lista di Eventi)
     */
    @GetMapping("/eventi/{matricola}")
    public ResponseEntity<?> getAllUserEventi(@PathVariable String matricola){
        return userService.getEventiByUserAdmin(matricola);
    }
}
