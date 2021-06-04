package javaframework.order_food_manage.dto;

import lombok.Data;

public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt(){ return jwt;}
}
