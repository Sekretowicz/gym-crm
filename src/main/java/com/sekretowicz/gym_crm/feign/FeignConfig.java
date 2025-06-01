package com.sekretowicz.gym_crm.feign;

import com.sekretowicz.gym_crm.auth.JwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void apply(RequestTemplate template) {
        System.out.println("Adding Authorization header to Feign request");
        template.header("Authorization", "Bearer "  + jwtUtil.generateToken("gym-crm"));
    }
}
