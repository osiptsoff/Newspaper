package ru.spb.nicetu.newspaper.api.service.exception;

import lombok.experimental.StandardException;

/**
 * <p>Exception at request to register user with username which is already taken.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@StandardException
public class UsernameTakenException extends RuntimeException {
    
}
