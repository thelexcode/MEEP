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
