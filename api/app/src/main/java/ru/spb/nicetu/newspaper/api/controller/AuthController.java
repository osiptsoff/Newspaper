package ru.spb.nicetu.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.TokenDto;
import ru.spb.nicetu.newspaper.api.dto.UserAuthenticateDto;
import ru.spb.nicetu.newspaper.api.dto.UserRegistrationDto;
import ru.spb.nicetu.newspaper.api.service.AuthService;

/**
 * <p>Controller for '/auth' endpoint.</p>
 *
 * <p>Provides API for registration, authentication and logging out.</p>
    * @author Nikita Osiptsov
    * @see {@link AuthService}
 * @since 1.0
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@Valid @RequestBody UserRegistrationDto userDto) {
        authService.register(userDto.getLogin(),
            userDto.getPassword(),
            userDto.getName(),
            userDto.getLastName());
    }

    @PostMapping()
    public TokenDto authenticate(@Valid @RequestBody UserAuthenticateDto dto) {
        String token = authService.authenticate(dto.getLogin(), dto.getPassword());

        return new TokenDto("refresh", token);
    }

    @PostMapping("/refresh")
    public TokenDto refresh(@Valid @RequestBody TokenDto tokenDto) {
        String token = authService.refresh(tokenDto.getValue());

        return new TokenDto("access", token);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto.getValue());
    }
}
