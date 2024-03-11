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
import ru.spb.nicetu.newspaper.api.service.facade.AuthServiceFacade;

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
    private final AuthServiceFacade authServiceFacade;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@Valid @RequestBody UserRegistrationDto userDto) {
        authServiceFacade.register(userDto);
    }

    @PostMapping()
    public TokenDto authenticate(@Valid @RequestBody UserAuthenticateDto dto) {
        return authServiceFacade.authenticate(dto);
    }

    @PostMapping("/refresh")
    public TokenDto refresh(@Valid @RequestBody TokenDto tokenDto) {
        return authServiceFacade.refresh(tokenDto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody TokenDto tokenDto) {
        authServiceFacade.logout(tokenDto);
    }
}
