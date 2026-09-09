package com.galicio.smartpark.controller;

import com.galicio.smartpark.dto.AuthRequest;
import com.galicio.smartpark.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLogin_ValidCredentials_ReturnsToken() {
        AuthRequest request = new AuthRequest();
        request.setUsername("galicio");
        request.setPassword("smartpark");

        when(jwtTokenUtil.generateToken("galicio")).thenReturn("mock-jwt-token");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testLogin_InvalidCredentials_ReturnsUnauthorized() {
        AuthRequest request = new AuthRequest();
        request.setUsername("wronguser");
        request.setPassword("wrongpass");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}