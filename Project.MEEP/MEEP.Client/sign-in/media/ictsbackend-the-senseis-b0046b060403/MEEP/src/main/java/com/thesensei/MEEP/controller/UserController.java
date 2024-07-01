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

    //Registrazione  (primo login)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request, HttpServletRequest httpRequest,HttpServletRequest httpServletRequest) throws MessagingException, IOException {
        return userService.signup(request, httpRequest.getAttribute("matricola").toString(), httpServletRequest.getAttribute("email").toString());
    }

    //Esegui il login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request){
        return userService.login(request);
    }

    //Modifica foto profilo
    @PostMapping("/edit-foto")
    public ResponseEntity<?> editProfilePicture(@RequestParam MultipartFile file, HttpServletRequest httpRequest){
        return userService.editProfilePicture(file,httpRequest.getAttribute("matricola").toString());
    }

    //Get foto profilo dell'utente loggato
    @GetMapping("/foto")
    public ResponseEntity<?> getProfilePicture(HttpServletRequest httpRequest) throws IOException {
        return userService.getProfilePicture(httpRequest.getAttribute("matricola").toString());
    }

    //Get foto profilo di un utente data la sua matricola
    @GetMapping("/{matricola}/foto")
    public ResponseEntity<?> getProfilePicture(@PathVariable String matricola) throws IOException {
        return userService.getProfilePicture(matricola);
    }

    //Modifica i dati dell'utente
    @PutMapping("/edit")
    public ResponseEntity<?> editUserData(@RequestBody @Valid EditUserRequest request, HttpServletRequest httpRequest){
        return userService.editUserData(request, httpRequest.getAttribute("matricola").toString());
    }

    //Modifica i dati dell'indirizzo
    @PutMapping("/indirizzo/edit")
    public ResponseEntity<?> editIndirizzo(@RequestBody @Valid IndirizzoRequest request, HttpServletRequest httpRequest){
        return indirizzoService.editIndirizzo(request,httpRequest.getAttribute("matricola").toString());
    }

    //Get dati utente data la matricola
    @GetMapping("/info/{matricola}")
    public ResponseEntity<?> getInfo(@PathVariable String matricola) {
        return userService.infoUtente(matricola);
    }

    //Get dati dell'utente loggato
    @GetMapping("/info")
    public ResponseEntity<?> getInfoLoggedUser(HttpServletRequest httpRequest) {
        return userService.infoUtente(httpRequest.getAttribute("matricola").toString());
    }

    //Get tutti gli eventi di cui l'utente è amministratore
    @GetMapping("/eventi")
    public ResponseEntity<?> getAllUserEventi(HttpServletRequest httpRequest){
        return userService.getEventiByUserAdmin(httpRequest.getAttribute("matricola").toString());
    }
}
