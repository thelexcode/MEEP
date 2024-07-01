package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.EventoRepository;
import com.thesensei.MEEP.repository.IndirizzoRepository;
import com.thesensei.MEEP.repository.UserRepository;
import com.thesensei.MEEP.request.EditUserRequest;
import com.thesensei.MEEP.request.LoginRequest;
import com.thesensei.MEEP.request.SignupRequest;
import com.thesensei.MEEP.response.LoginResponse;
import com.thesensei.MEEP.response.UserResponse;
import jakarta.mail.MessagingException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final IndirizzoRepository indirizzoRepository;
    private final ImageService imageService;

    private final EventoRepository eventoRepository;

    public UserService(UserRepository userRepository, TokenService tokenService, IndirizzoRepository indirizzoRepository, ImageService imageService, EventoRepository eventoRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.indirizzoRepository = indirizzoRepository;
        this.imageService = imageService;
        this.eventoRepository = eventoRepository;
    }

    /**
     * login() -> Date le credenziali passate nella request, consente all'utente di autenticarsi.
     *            Nel body della response, viene restituito il token, la matricola e il valore booleano "isFirstLogin"
     * @param request (matricola, password)
     * @return response ->  LoginResponse (String matricola, String token, boolean isFirstLogin)
     */
    public ResponseEntity<?> login(LoginRequest request) {
        Optional<User> u = userRepository.findByMatricolaAndPassword(request.getMatricola(), new DigestUtils("SHA3-256").digestAsHex(request.getPassword()));   //controllo delle credenziali
        if (!u.isEmpty()) {
            User user = u.get();
            String token = tokenService.createToken(user.getMatricola(),user.getEmail());      //creazione del token per l'autenticazione
            LoginResponse response = new LoginResponse(user.getMatricola(), token);
            if (user.getUsername() == null) {   //se è il primo login dell'utente...
                response.setFirstLogin(true);
                return new ResponseEntity(response, HttpStatus.OK);
            }
            return new ResponseEntity(response, HttpStatus.OK);    //operazione di login completata con successo
        } else
            return new ResponseEntity("Errore: credenziali errate", HttpStatus.BAD_REQUEST);
    }

    /**
     *  signup() -> Inserendo un username, la nuova password (2 volte per controllo delle credenziali) e la biografia (opzionale),
     *              consente all'utente di completare la registrazione (Il primo login dell'utente)
     *
     * @param request (newPassword, checkPassword, username, biografia)
     * @param matricola
     * @param email
     * @return response
     * @throws MessagingException
     * @throws IOException
     */
    public ResponseEntity<?> signup(SignupRequest request, String matricola ,String email) throws MessagingException, IOException {
        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()) {
            if (!request.getNewPassword().equals(request.getCheckPassword()))
                return new ResponseEntity("Errore: le credenziali non corrispondono", HttpStatus.BAD_REQUEST);
            else if (userRepository.existsByusername(request.getUsername()))
                return new ResponseEntity("Errore: username già utilizzato", HttpStatus.BAD_REQUEST);
            else {
                //salvataggio su db

                User user = u.get();

                String username = request.getUsername();
                String password = request.getNewPassword();

                user.setUsername(username);
                user.setPassword(new DigestUtils("SHA3-256").digestAsHex(password));
                user.setBiografia(request.getBiografia());

                userRepository.save(user);

                //invio email

                String recipientEmail = email;
                String subject = "Registrazione completata";
                String body = "Grazie per esserti registrato!\n\n"
                        + "Il tuo username: " + username + "\n"
                        + "La tua password: " + password + "\n";

                EmailService.sendEmail(recipientEmail, subject, body);

                return new ResponseEntity("Registrazione completata!", HttpStatus.OK);
            }
        }else
            return new ResponseEntity("Errore: Impossibile trovare l'utente",HttpStatus.NOT_FOUND);
    }

    /**
     * editUserData() ->Permette di modificare un utente salvato sul db.
     *                  Dati in input tutti i dati dell'utente da modificare e la sua matricola,
     *                  dopo aver controllato l'esistenza dell'utente sul db, aggiorna tutti i dati
     * @param request  (newPassword, checkPassword, username, biografia ,email, telefono)
     * @param matricola
     * @return User (oggetto User )
     */
    public ResponseEntity<?> editUserData(EditUserRequest request, String matricola){

        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()){
            User user = u.get();
            if (!request.getUsername().equals(user.getUsername())){
                String username = request.getUsername();
                if (userRepository.existsByusername(username))
                    return new ResponseEntity("Errore: username '"+username+"' già utilizzato", HttpStatus.BAD_REQUEST);
                else
                    user.setUsername(username);
            }

            String password= new DigestUtils("SHA3-256").digestAsHex(request.getNewPassword());
            if (!password.equals(user.getPassword())){
                if (!request.getNewPassword().equals(request.getCheckPassword()))
                    return new ResponseEntity("Errore: le credenziali non corrispondono", HttpStatus.BAD_REQUEST);
                else
                    user.setPassword(password);
            }

            if(!request.getBiografia().equals(user.getBiografia()))
                user.setBiografia(request.getBiografia());

            if(!request.getTelefono().equals(user.getTelefono()))
                user.setTelefono(request.getTelefono());

            if(!request.getEmail().equals(user.getEmail()))
                user.setEmail(request.getEmail());

            userRepository.save(user);
            return new ResponseEntity(user,HttpStatus.OK);

        }else
            return new ResponseEntity("Errore: Impossibile trovare l'utente",HttpStatus.NOT_FOUND);

    }

    /**
     * img properties
     */
    @Value("${user.profilepicture.size}")
    private int imageSize;

    @Value("${user.profilepicture.width}")
    private int imageWidth;

    @Value("${user.profilepicture.height}")
    private int imageHeight;

    @Value("${user.profilepicture.path}")
    private String imagePath;

    /**
     * editProfilePicture() -> permette di modificare l'immagine di profilo dell'utente
     *                         Dati in input la matricola e un file, vengono effettuati una serie di controlli sul file ricevuto (estensioni del file, dimensioni massime e corretto salvataggio sul server) e,
     *                         se tutte le condizioni sono rispettate, permette di modificare il riferimento al path dell'immagine sul db (il campo "foto_profilo")
     * @param file (MultipartFile)
     * @param matricola
     * @return response
     */
    public ResponseEntity<?> editProfilePicture(MultipartFile file, String matricola){
        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()){
            if(!imageService.checkExtensions(file))
                return new ResponseEntity("Errore: Il file non rispetta le estensioni consentite",HttpStatus.BAD_REQUEST);
            if(!imageService.checkImageSize(file,imageSize))
                return new ResponseEntity("Errore: Il file inserito è maggiore di "+imageSize+" bytes oppure è vuoto",HttpStatus.BAD_REQUEST);
            BufferedImage image = imageService.fromMultipartFileToBufferedImage(file);
            if(!imageService.checkDimensioni(image,imageHeight,imageWidth))
                return new ResponseEntity("Errore: l'immagine ha dimensioni errate",HttpStatus.BAD_REQUEST);
            String nomeFile=matricola+"_"+file.getOriginalFilename();
            if(!imageService.uploadFile(file,nomeFile,imagePath))
                return new ResponseEntity("Errore nell'upload dell'immagine",HttpStatus.BAD_REQUEST);
            else{
                userRepository.updateFotoProfilo(matricola,nomeFile);
                return new ResponseEntity("Foto profilo cambiata con successo",HttpStatus.OK);
            }
        }else
            return new ResponseEntity("Errore: Impossibile trovare l'utente",HttpStatus.NOT_FOUND);

    }

    /**
     * getProfilePicture() -> dato in input la matricola, restituisce la foto profilo se esiste sul server
     * @param matricola
     * @return response  ( byte[]  bytes della foto profilo)
     * @throws IOException
     */
    public ResponseEntity<?> getProfilePicture(String matricola) throws IOException {
        if(userRepository.existsById(matricola)){
            String imgName = userRepository.getFotoProfilo(matricola);
            Path pathImage = Paths.get(imagePath+imgName);
            try{
                byte[] bytes = Files.readAllBytes(pathImage);
                return new ResponseEntity(bytes,HttpStatus.OK);
            }catch (IOException e){
                return new ResponseEntity("Errore: Immagine non trovata",HttpStatus.NOT_FOUND);
            }
        }else
            return new ResponseEntity("Errore: Non è stato possibile trovare l'utente.", HttpStatus.NOT_FOUND);
    }

    /**
     * infoUtente() -> data la matricola dell'utente, ne restituisce tutti i dati
     * @param matricola
     * @return response (UserResponse , con tutti i dati dell'utente e del suo indirizzo)
     */
    public ResponseEntity<?> infoUtente(String matricola){
        if(userRepository.existsById(matricola)){
            UserResponse userResponse = fromEntityToResponse(matricola);
            if(userResponse != null) {
                return new ResponseEntity(userResponse,HttpStatus.OK);
            }else{
                return new ResponseEntity("Errore: Dati utente non corretti", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return new ResponseEntity("Errore: Non è stato possibile trovare l'utente.", HttpStatus.NOT_FOUND);
    }

    /**
     * fromEntityToResponse() -> Data la matricola, estrapola tutti i dati dell'utente dal db (compresi i dati dell'indirizzo) e crea un nuovo oggetto UserResponse
     * @param matricola
     * @return UserResponse ( matricola, username, email, nome, cognome, telefono, dataNascita, biografia, via, cap, comune, provincia)
     */
    private UserResponse fromEntityToResponse(String matricola){
        Optional<User> u = userRepository.findById(matricola);
        Optional<Indirizzo> i = indirizzoRepository.findByIndirizzoIdUserMatricola(matricola);

        if(u.isEmpty()){
            return null;
        }else if(i.isEmpty()){
            return null;
        }else{
            User user = u.get();
            Indirizzo indirizzo= i.get();
            UserResponse userResponse = new UserResponse(
                    matricola,
                    user.getUsername(),
                    user.getEmail(),
                    user.getNome(),
                    user.getCognome(),
                    user.getTelefono(),
                    user.getDataNascita(),
                    user.getBiografia(),
                    indirizzo.getVia(),
                    indirizzo.getCap(),
                    indirizzo.getComune(),
                    indirizzo.getProvincia());
            return userResponse;
        }
    }

    /**
     * getEventiByUserAdmin() -> dato in input la matricola dell'utente, restituisce tutti gli eventi di cui è amministratore
     * @param matricola
     * @return response (List<Evento>)
     */
    public ResponseEntity<?> getEventiByUserAdmin(String matricola){
        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()){
            List<Evento> eventi = eventoRepository.findAllByUtenteAdmin(u.get());
            return new ResponseEntity(eventi, HttpStatus.OK);
        }else
            return new ResponseEntity("Errore: Non è stato possibile trovare l'utente.", HttpStatus.NOT_FOUND);
    }

}
