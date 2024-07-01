package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.request.QRCodeRequest;
import com.thesensei.MEEP.service.QRCodeService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    /**
     * partecipateAndGenerateQRCode() -> Passati in input tutti i dati dei partecipanti e il codice dell'evento, permette di aggiungere al db le partecipazioni ad un evento
     * @param request (int n_accompagnatori, int codice_evento, String[] nomiAccompagnatori,  String[] emailAccompagnatori)
     * @param httpServletRequest  (per passare la matricola dell'utente autenticato)
     * @return response
     * @throws MessagingException
     */
    @PostMapping("/qr")
    public ResponseEntity<?> partecipateAndGenerateQRCode(@RequestBody @Valid QRCodeRequest request, HttpServletRequest httpServletRequest) throws MessagingException {
        return qrCodeService.partecipateAndGenerateQRCode(request, httpServletRequest.getAttribute("matricola").toString(), httpServletRequest.getAttribute("email").toString());
    }
}
