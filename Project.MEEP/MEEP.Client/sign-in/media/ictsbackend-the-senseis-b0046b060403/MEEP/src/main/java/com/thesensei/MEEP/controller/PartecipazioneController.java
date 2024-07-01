package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.request.QRCodeRequest;
import com.thesensei.MEEP.service.QRCodeService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("partecipazione")
public class PartecipazioneController {

    private final QRCodeService qrCodeService;

    public PartecipazioneController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/qr")
    public ResponseEntity<?> partecipateAndGenerateQRCode(@RequestBody QRCodeRequest request, HttpServletRequest httpServletRequest) throws MessagingException {
        return qrCodeService.partecipateAndGenerateQRCode(request, httpServletRequest.getAttribute("matricola").toString(), httpServletRequest.getAttribute("email").toString());
    }
}
