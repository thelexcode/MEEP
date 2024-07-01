package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.repository.EventoRepository;
import com.thesensei.MEEP.repository.PartecipazioneRepository;
import com.thesensei.MEEP.repository.UserRepository;
import com.thesensei.MEEP.request.QRCodeRequest;
import com.thesensei.MEEP.service.impl.QRCodeGeneratorImpl;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class QRCodeService {

    private final QRCodeGeneratorImpl qrCodeGeneratorImpl;
    private final EmailService emailService;
    private final PartecipazioneRepository partecipazioneRepository;
    private final UserRepository userRepository;
    private final EventoRepository eventoRepository;



    public QRCodeService(QRCodeGeneratorImpl qrCodeGeneratorImpl, EmailService emailService, PartecipazioneRepository partecipazioneRepository, UserRepository userRepository, EventoRepository eventoRepository) {
        this.qrCodeGeneratorImpl = qrCodeGeneratorImpl;
        this.emailService = emailService;
        this.partecipazioneRepository = partecipazioneRepository;
        this.userRepository = userRepository;
        this.eventoRepository = eventoRepository;
    }

    /*
        Il  metodo partecipateAndGenerateQRCode permette dati in input il codice evento, i dati degli accompagnatori, la matricola e l'email dell'utente,
        di generare un codice Qr con i dati sopraindicati. Una volta generato il qr code, tutti i dati  verranno salvati sul db e verrà inviata un email di conferma di registrazione ad evento sia al dipendente che ai suoi eventuali accompagnatori
    */

    public ResponseEntity<?> partecipateAndGenerateQRCode(QRCodeRequest request, String matricola,String email) throws MessagingException {
        int codiceEvento = request.getCodice_evento();
        LocalDateTime oggi = LocalDateTime.now();

        //controlli prima del salvataggio dati
        if(userRepository.existsById(matricola)){
            Optional<Evento> evento = eventoRepository.findByCodiceEvento(codiceEvento);
            if(!evento.isEmpty()){
                int nAccompagnatori= request.getN_accompagnatori();
                if(nAccompagnatori>eventoRepository.getNumeroAccompagnatori(codiceEvento))
                    return new ResponseEntity("Errore: numero massimo di accompagnatori superato", HttpStatus.BAD_REQUEST);
                else {
                    List<Evento> eventiDisponibili = eventoRepository.getEventiDisponibiliNoAdmin(matricola,oggi.toLocalDate(),oggi.toLocalTime());
                    if(eventiDisponibili.contains(evento.get())) {
                        if(eventoRepository.getNumeroPartecipanti(codiceEvento)+nAccompagnatori+1<=evento.get().getMaxPartecipanti()) {
                            // Genera il codice QR Dipendente
                            BufferedImage qrImage = qrCodeGeneratorImpl.generateQRCode(nAccompagnatori, codiceEvento, matricola);

                            // Converte l'immagine QR in base64
                            String encodedImage = convertImageToBase64(qrImage);
                            if(!sendEmailPartecipazione(email,encodedImage)) {
                                return  new ResponseEntity("Errore nell'invio della mail di conferma", HttpStatus.INTERNAL_SERVER_ERROR);
                            }

                            // Salva i dati nel database
                            partecipazioneRepository.saveToDatabase(false, codiceEvento, matricola, encodedImage);
                            System.out.println("Email Dipendente inviata con successo");

                            if(nAccompagnatori>0){
                                String[] nomiAccompagnatori = request.getNomiAccompagnatori();
                                String[] emailAccompagnatori = request.getEmailAccompagnatori();
                                for(int i=0;i<nomiAccompagnatori.length;i++){
                                    BufferedImage qrImageAccompagnatore = qrCodeGeneratorImpl.generateQRCodeAccompagnatore(codiceEvento,matricola,nomiAccompagnatori[i],emailAccompagnatori[i]);
                                    String encodedImageAccompagnatore = convertImageToBase64(qrImageAccompagnatore);
                                    partecipazioneRepository.saveToDatabase(true, codiceEvento, matricola, encodedImage);
                                    if(!sendEmailPartecipazione(emailAccompagnatori[i],encodedImageAccompagnatore)) {
                                        return  new ResponseEntity("Errore nell'invio della mail di conferma", HttpStatus.INTERNAL_SERVER_ERROR);
                                    }
                                    System.out.println("Email Accompagnatore inviata con successo");
                                }
                            }
                            return new ResponseEntity("Operazione completata con successo", HttpStatus.OK);
                        }else
                            return new ResponseEntity("Errore: non è possibile partecipare all'evento",HttpStatus.BAD_REQUEST);
                    }else
                        return new ResponseEntity("Errore: non è possibile partecipare all'evento",HttpStatus.BAD_REQUEST);
                }
            }else
                return new ResponseEntity("Errore: L'evento non esiste", HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity("Errore: L'utente è inesistente, riesegui l'accesso", HttpStatus.NOT_FOUND);
    }


    private String convertImageToBase64(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            outputStream.close();
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean sendEmailPartecipazione(String emailDestinatario, String encodedImage) throws MessagingException {
        try {
            // Invia l'email
            String recipientEmail = emailDestinatario; // Indirizzo email del destinatario
            String subject = "Partecipazione"; // Oggetto dell'email
            String body = "In allegato il codice QR da mostrare all'evento"; // Corpo dell'email
            byte[] qrCodeImageBytes = Base64.getDecoder().decode(encodedImage); // Converte l'immagine QR da Base64 a byte[]


            emailService.sendEmailWithQRCode(recipientEmail, subject, body, qrCodeImageBytes);
            return true;
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email:");
            e.printStackTrace();
            return false;
        }
    }
}
