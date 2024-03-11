package ru.spb.nicetu.newspaper.api.service.facade;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.TokenDto;
import ru.spb.nicetu.newspaper.api.dto.UserAuthenticateDto;
import ru.spb.nicetu.newspaper.api.dto.UserRegistrationDto;
import ru.spb.nicetu.newspaper.api.service.AuthService;

/**
 * <p>{@link AuthServiceFacade} implementation</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link AuthService}
 * @since 1.1
 */
@Component
@RequiredArgsConstructor
public class AuthServiceFacadeImpl implements AuthServiceFacade {
    private final AuthService authService;

    @Override
    public void register(UserRegistrationDto userDto) {
        authService.register(userDto.getLogin(),
            userDto.getPassword(),
            userDto.getName(),
            userDto.getLastName());
    }

    @Override
    public TokenDto authenticate(UserAuthenticateDto dto) {
        String token = authService.authenticate(dto.getLogin(), dto.getPassword());

        return new TokenDto("refresh", token);
    }

    @Override
    public TokenDto refresh(TokenDto tokenDto) {
        String token = authService.refresh(tokenDto.getValue());

        return new TokenDto("access", token);
    }

    @Override
    public void logout(TokenDto tokenDto) {
        authService.logout(tokenDto.getValue());
    }
    
}
