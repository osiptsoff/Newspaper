package ru.spb.nicetu.newspaper.api.service.facade;

import ru.spb.nicetu.newspaper.api.dto.TokenDto;
import ru.spb.nicetu.newspaper.api.dto.UserAuthenticateDto;
import ru.spb.nicetu.newspaper.api.dto.UserRegistrationDto;
import ru.spb.nicetu.newspaper.api.service.AuthService;

/**
 * <p>Class which wraps {@link AuthService} in order to provide
 * simplier interface for interaction.</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link AuthService}
 * @since 1.2
 */
public interface AuthServiceFacade {
    void register(UserRegistrationDto userDto);
    TokenDto authenticate(UserAuthenticateDto dto);
    TokenDto refresh(TokenDto tokenDto);
    void logout(TokenDto tokenDto);
    Integer getRefreshLifespawn();
}
