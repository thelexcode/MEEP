package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Accompagnatore;
import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.Partecipazione;
import com.thesensei.MEEP.repository.*;
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

    private final AccompagnatoreRepository accompagnatoreRepository;
    private final IndirizzoRepository indirizzoRepository;



    public QRCodeService(QRCodeGeneratorImpl qrCodeGeneratorImpl, EmailService emailService, PartecipazioneRepository partecipazioneRepository, UserRepository userRepository, EventoRepository eventoRepository, AccompagnatoreRepository accompagnatoreRepository, IndirizzoRepository indirizzoRepository) {
        this.qrCodeGeneratorImpl = qrCodeGeneratorImpl;
        this.emailService = emailService;
        this.partecipazioneRepository = partecipazioneRepository;
        this.userRepository = userRepository;
        this.eventoRepository = eventoRepository;
        this.accompagnatoreRepository = accompagnatoreRepository;
        this.indirizzoRepository = indirizzoRepository;
    }

    /**
     * partecipateAndGenerateQRCode() -> permette di salvare i dati delle partecipazioni e genera i qrcode.
     *                                   Dati in input il codice evento, i dati degli accompagnatori, la matricola e l'email dell'utente,
     *                                   Controlla se i dati inseriti sono corretti e controlla se l'utente può partecipare all'evento.
     *                                   Dopo i controlli, vengono generati i qrcode per tutti i partecipanti (Dipendente + accompagnatori) e
     *                                   tutti i dati  vengono salvati sul db.
     *                                   Verrà infine inviata un email di conferma partecipazione ad evento a tutti i partecipanti con il riepilogo dell'evento
     *                                   e in allegato il rispettivo qrcode.
     *
     * @param request (int n_accompagnatori, int codice_evento, String[] nomiAccompagnatori, String[] emailAccompagnatori)
     * @param matricola (del dipendente)
     * @param email (del dipendente)
     * @return response
     * @throws MessagingException
     */
    public ResponseEntity<?> partecipateAndGenerateQRCode(QRCodeRequest request, String matricola,String email) throws MessagingException {
        int codiceEvento = request.getCodice_evento();
        LocalDateTime oggi = LocalDateTime.now();

        //controlli prima del salvataggio dati
        if(userRepository.existsById(matricola)){
            Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
            if(!e.isEmpty()){
                Evento evento = e.get();
                int nAccompagnatori= request.getN_accompagnatori();
                if(nAccompagnatori>eventoRepository.getNumeroAccompagnatori(codiceEvento))
                    return new ResponseEntity("Errore: numero massimo di accompagnatori superato", HttpStatus.BAD_REQUEST);
                else {
                    //controlla che l'evento è tra quelli disponibili per il dipendente
                    List<Evento> eventiDisponibili = eventoRepository.getEventiDisponibiliNoAdmin(matricola,oggi.toLocalDate(),oggi.toLocalTime());
                    if(eventiDisponibili.contains(evento)) {
                        //controlla che l'utente non sia già partecipante dell'evento
                        Optional<Partecipazione> p = partecipazioneRepository.getPartecipazioneDipendente(codiceEvento,matricola);
                        if(p.isEmpty()) {
                            //controlla che il dipendente + gli accompagnatori non superino il numero massimo di partecipanti
                            if(eventoRepository.getNumeroPartecipanti(codiceEvento)+nAccompagnatori+1<=evento.getMaxPartecipanti()) {

                                // Genera il codice QR Dipendente
                                BufferedImage qrImage = qrCodeGeneratorImpl.generateQRCode(nAccompagnatori, codiceEvento, matricola);

                                // Converte l'immagine QR in base64
                                String encodedImage = convertImageToBase64(qrImage);
                                if(!sendEmailPartecipazione(email, evento, encodedImage)) {
                                    return  new ResponseEntity("Errore nell'invio della mail di conferma", HttpStatus.INTERNAL_SERVER_ERROR);
                                }

                                // Salva i dati nel database
                                partecipazioneRepository.saveToDatabase(null, codiceEvento, matricola, encodedImage);
                                System.out.println("Email Dipendente inviata con successo");

                                if(nAccompagnatori>0){
                                    String[] nomiAccompagnatori = request.getNomiAccompagnatori();
                                    String[] emailAccompagnatori = request.getEmailAccompagnatori();
                                    //genera il qrcode per ogni accompagnatore e salva i dati sul db
                                    for(int i=0;i<nomiAccompagnatori.length;i++){
                                        Accompagnatore a = new Accompagnatore(nomiAccompagnatori[i],emailAccompagnatori[i]);
                                        accompagnatoreRepository.save(a);
                                        BufferedImage qrImageAccompagnatore = qrCodeGeneratorImpl.generateQRCodeAccompagnatore(codiceEvento,matricola,a);
                                        String encodedImageAccompagnatore = convertImageToBase64(qrImageAccompagnatore);
                                        partecipazioneRepository.saveToDatabase(a.getId(), codiceEvento, matricola, encodedImage);
                                        if(!sendEmailPartecipazione(a.getEmail(), evento, encodedImageAccompagnatore)) {
                                            return  new ResponseEntity("Errore nell'invio della mail di conferma", HttpStatus.INTERNAL_SERVER_ERROR);
                                        }
                                        System.out.println("Email Accompagnatore inviata con successo");
                                    }
                                }
                                return new ResponseEntity("Operazione completata con successo", HttpStatus.OK);
                            }else
                                return new ResponseEntity("Errore: non è possibile partecipare all'evento", HttpStatus.BAD_REQUEST);
                        }else
                            return new ResponseEntity("Errore: l'utente è già partecipante",HttpStatus.BAD_REQUEST);
                    }else
                        return new ResponseEntity("Errore: non è possibile partecipare all'evento",HttpStatus.BAD_REQUEST);
                }
            }else
                return new ResponseEntity("Errore: L'evento non esiste", HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity("Errore: L'utente è inesistente, riesegui l'accesso", HttpStatus.NOT_FOUND);
    }


    /**
     * convertImageToBase64() -> dato in input una BufferedImage, la converte in binario
     * @param image   (BufferedImage)
     * @return byte[]
     */
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

    /**
     * sendEmailPartecipazione() -> invia un'email con tutti i dettagli della partecipazione
     *                              Data l'email del destinatario, l'evento e l'immagine del qrcode, viene inviata una email con il riepilogo sui dettagli dell'evento e allegato il qrcode
     * @param emailDestinatario
     * @param evento
     * @param encodedImage
     * @return boolean (restituisce true se l'operazione è andata a buonfine)
     * @throws MessagingException
     */
    private boolean sendEmailPartecipazione(String emailDestinatario, Evento evento, String encodedImage) throws MessagingException {
        try {
            Optional<Indirizzo> indirizzo = indirizzoRepository.getIndirizzoByEvento(evento.getCodiceEvento());
            if(!indirizzo.isEmpty()) {
                // Invia l'email
                String recipientEmail = emailDestinatario; // Indirizzo email del destinatario
                String subject = "Partecipazione"; // Oggetto dell'email
                String body = "Riepilogo Evento: \n" + evento+ "\nIndirizzo: "+indirizzo.get()+
                        "\n\nIn allegato il codice QR da mostrare all'evento"; // Corpo dell'email
                byte[] qrCodeImageBytes = Base64.getDecoder().decode(encodedImage); // Converte l'immagine QR da Base64 a byte[]

                emailService.sendEmailWithQRCode(recipientEmail, subject, body, qrCodeImageBytes);   //invia l'email
                return true;
            }else
                return false;

        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email:");
            e.printStackTrace();
            return false;
        }
    }
}
