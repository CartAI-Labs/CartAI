package com.bikemmerce.commerce.domain.ports.security.services;

public interface PasswordEncoderPort {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

}
