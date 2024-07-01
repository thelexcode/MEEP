package com.thesensei.MEEP.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin
public class WebConfig implements WebMvcConfigurer {

    /**
     * addInterceptors() -> permette di gestire gli Interceptor delle chiamate.
     *                      Intercetta tutte le chiamate (a parte la richiesta di login), per verificare la validitò del token
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")      //path delle request che devono essere intercettate
                .excludePathPatterns("/user/login"); //path delle request escluse dall'interceptor

    }

}
