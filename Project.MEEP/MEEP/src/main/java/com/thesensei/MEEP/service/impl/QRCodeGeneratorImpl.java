package com.thesensei.MEEP.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.thesensei.MEEP.entity.Accompagnatore;
import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.IndirizzoRepository;
import com.thesensei.MEEP.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Optional;

@Component
public class QRCodeGeneratorImpl {

    private final int QR_CODE_WIDTH = 300;
    private final int QR_CODE_HEIGHT = 300;

    private final UserRepository userRepository;
    private final IndirizzoRepository indirizzoRepository;

    public QRCodeGeneratorImpl(UserRepository userRepository, IndirizzoRepository indirizzoRepository) {
        this.userRepository = userRepository;
        this.indirizzoRepository = indirizzoRepository;
    }


    /**
     * generateQRCode() -> permette di generare il QRcode della partecipazione ad un evento di un dipendente, contiene i dati dell'utente, dati dell' evento a cui vuole partecipare e
     *                     il numero degli accompagnatori
     * @param n_accompagnatori
     * @param codice_evento
     * @param matricola
     * @return BufferedImage ( qrcode )
     */
    public BufferedImage generateQRCode(int n_accompagnatori, int codice_evento, String matricola) {
        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()){

                String qrData = "Partecipante dipendente: " + u.get().getNome() + " " + u.get().getCognome() + ", \nNumero accompagnatori: " + n_accompagnatori + ", \nCodice Evento: " + codice_evento;
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                    return MatrixToImageWriter.toBufferedImage(bitMatrix);
                } catch (WriterException e) {
                    e.printStackTrace();
                    return null;
                }
        }
        return null;

    }


    /**
     * generateQRCodeAccompagnatore() -> permette di generare il QRcode della partecipazione ad un evento di un accompagnatore, contiene i dati dell'accompagnatore (nominativo ed email)
                                         dati del dipendente di riferimento e il codice dell'evento
     * @param codice_evento
     * @param matricola
     * @param accompagnatore
     * @return BufferedImage ( qrcode )
     */
    public BufferedImage generateQRCodeAccompagnatore(int codice_evento, String matricola, Accompagnatore accompagnatore) {
        Optional<User> u = userRepository.findById(matricola);
        if(!u.isEmpty()){
                String qrData = "Partecipante: " + accompagnatore.getNominativo() + ", \nEmail: " + accompagnatore.getEmail() + ", \nDipendente Responsabile: " + u.get().getNome() + " " + u.get().getCognome() + ", \nCodice Evento: " + codice_evento ;
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                    return MatrixToImageWriter.toBufferedImage(bitMatrix);
                } catch (WriterException e) {
                    e.printStackTrace();
                    return null;
                }
        }
        return null;
    }

}
