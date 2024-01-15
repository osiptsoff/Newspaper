package ru.osiptsoff.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.TokenDto;
import ru.osiptsoff.newspaper.api.dto.UserAuthenticateDto;
import ru.osiptsoff.newspaper.api.dto.UserRegistrationDto;
import ru.osiptsoff.newspaper.api.service.AuthService;

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
                userDto.getLastName()
            );
    }

    @PostMapping()
    public TokenDto authenticate(@Valid @RequestBody UserAuthenticateDto dto) {
        String token = authService.authenticate(dto.getLogin(), dto.getPassword());

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
}
