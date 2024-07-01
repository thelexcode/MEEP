package com.thesensei.MEEP.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Optional;

@Component
public class QRCodeGeneratorImpl {

    private final int QR_CODE_WIDTH = 300;
    private final int QR_CODE_HEIGHT = 300;

    private final UserRepository userRepository;

    public QRCodeGeneratorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BufferedImage generateQRCode(int n_accompagnatori, int codice_evento, String codice_utente) {
        Optional<User> u = userRepository.findById(codice_utente);
        if(!u.isEmpty()){
            String qrData = "Partecipante dipendente: " + u.get().getNome()+" "+u.get().getCognome()+", Numero accompagnatori: "+n_accompagnatori + ", Codice Evento: " + codice_evento;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                return MatrixToImageWriter.toBufferedImage(bitMatrix);
            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }
        }else
            return null;

    }

    public BufferedImage generateQRCodeAccompagnatore(int codice_evento, String codice_utente, String nomeAccompagnatore, String emailAccompagnatore) {
        Optional<User> u = userRepository.findById(codice_utente);
        if(!u.isEmpty()){
            String qrData = "Partecipante: " +nomeAccompagnatore+", Email: "+emailAccompagnatore+", Dipendente Responsabile: "+u.get().getNome()+" "+u.get().getCognome()+", Codice Evento: " + codice_evento;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                return MatrixToImageWriter.toBufferedImage(bitMatrix);
            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }
        }else
            return null;

    }
}
