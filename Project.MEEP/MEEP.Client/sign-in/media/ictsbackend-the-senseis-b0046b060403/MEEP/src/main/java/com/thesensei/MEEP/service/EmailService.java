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

    public static void sendEmailLogin(String recipientEmail, String subject, String body) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gabriel.semeniuc@edu.itspiemonte.it");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(body);


        ClassPathResource imageResource = new ClassPathResource("img/logo/MEEP.jpg");
        helper.addAttachment("MEEP.png", imageResource.getFile());

        mailSender.send(message);
    }


}
