package com.thesensei.MEEP.config;

import com.thesensei.MEEP.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@CrossOrigin
public class JWTInterceptor implements HandlerInterceptor {

    //Webtoken handler
    TokenService tokenService = new TokenService();

    /**
     * preHandle() -> intercetta le request, verifica il JWT ed estrapola la matricola e l'email dell'utente autenticato.
     *                se l'autenticazione fallisce (token non valido quindi non riesce ad estrarre la matricola ), resituisce false e interrompe la gestione della richiesta
     *                altrimenti procede con l'esecuzione
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return boolean (indica se la richiesta può essere gestita dall'oggetto handler o se deve essere interrotta )
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS"))
            return true;
        else{

            String token = request.getHeader("Authorization");
            System.out.println("Pre Handler interceptor: "+request.getMethod()+" | "+request.getRequestURI());
            try{
                String matricola = tokenService.getMatricolaFromToken(token);
                if(matricola!=null) {
                    String email = tokenService.getEmailFromToken(token);
                    request.setAttribute("matricola", matricola);
                    request.setAttribute("email",email);
                    return true;
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
