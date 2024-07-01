package com.thesensei.MEEP.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    public static final String TOKEN_SECRET ="F1B8ABAFE8FA3445166DC3F28B6CA70F2705D68323221FCBF";
    public static final Algorithm  algorithm = Algorithm.HMAC256(TOKEN_SECRET);
    public static final int EXPIRE_AFTER = 10800;   //3 ore

    public String createToken(String matricola, String email){

        try{
            String token = JWT.create()
                    .withClaim("matricola",matricola)
                    .withClaim("email",email)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(EXPIRE_AFTER, ChronoUnit.SECONDS))
                    .sign(algorithm);
            return token;

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getMatricolaFromToken(String token){
        try {

            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token); //verifica il token
            String matricola = jwt.getClaim("matricola").asString();
            return matricola;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getEmailFromToken(String token){
        try {

            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token); //verifica il token
            String email = jwt.getClaim("email").asString();
            return email;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
