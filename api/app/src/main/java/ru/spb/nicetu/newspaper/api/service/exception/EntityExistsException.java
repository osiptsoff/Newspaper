package ru.spb.nicetu.newspaper.api.service.exception;

import lombok.experimental.StandardException;

/**
 * <p>Exception thrown at attempt to save already persistent entity which is not allowed to be rewritten.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@StandardException
public class EntityExistsException extends RuntimeException {
    
}
