package ru.spb.nicetu.newspaper.api.service.exception;

import lombok.experimental.StandardException;

/**
 * <p>Exception thrown at attempt to use non-persistent refresh token.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@StandardException
public class UnregistredTokenException extends RuntimeException {
    
}
