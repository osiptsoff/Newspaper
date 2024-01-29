package ru.spb.nicetu.newspaper.api.service.exception;

import lombok.experimental.StandardException;

/**
 * <p>Exception thrown when requested entity is not persistent.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@StandardException
public class MissingEntityException extends RuntimeException {
    
}
