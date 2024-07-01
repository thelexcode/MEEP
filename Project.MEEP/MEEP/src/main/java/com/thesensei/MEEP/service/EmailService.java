package com.thesensei.MEEP.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    private static JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * sendEmailWithQRCode() -> data l'email del ricevente,
     *                          permette di inviare una mail con allegato l'immagine del qrcode
     * @param recipientEmail
     * @param subject
     * @param body
     * @param qrCodeImage
     * @throws MessagingException
     */
    public void sendEmailWithQRCode(String recipientEmail, String subject, String body, byte[] qrCodeImage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("antonella.alberti@edu.itspiemonte.it");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(body);

        ByteArrayDataSource qrCodeDataSource = new ByteArrayDataSource(qrCodeImage, "image/png");
        helper.addAttachment("QRCode.png", qrCodeDataSource);

        mailSender.send(message);
    }

    /**
     * sendEmailLogin() -> data l'email del ricevente, soggetto e corpo, permette di inviare una email
     * @param recipientEmail
     * @param subject
     * @param body
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendEmail(String recipientEmail, String subject, String body) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gabriel.semeniuc@edu.itspiemonte.it");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(body);


        ClassPathResource imageResource = new ClassPathResource("img/logo/MEEP.jpg");
        helper.addAttachment("MEEP.png", imageResource.getFile());      //logo

        mailSender.send(message);
    }


}
