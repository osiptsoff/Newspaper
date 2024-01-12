package ru.osiptsoff.newspaper.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.TokenDto;
import ru.osiptsoff.newspaper.api.dto.UserDto;
import ru.osiptsoff.newspaper.api.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@Valid @RequestBody UserDto userDto) {
        authService.register(userDto.getLogin(), userDto.getPassword());
    }

    @PostMapping()
    public TokenDto authenticate(@Valid @RequestBody UserDto userDto) {
        String token = authService.authenticate(userDto.getLogin(), userDto.getPassword());

        return new TokenDto("refresh", token);
    }

    @GetMapping()
    public TokenDto refresh(@Valid @RequestBody TokenDto tokenDto) {
        String token = authService.refresh(tokenDto.getValue());

        return new TokenDto("access", token);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto.getValue());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> tokenExpiredExceptionHandler() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.FORBIDDEN.value());
        result.put("error", "Refresh token expired");

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
    }
}
