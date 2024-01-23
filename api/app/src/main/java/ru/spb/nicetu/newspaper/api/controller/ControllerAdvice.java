package ru.spb.nicetu.newspaper.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import io.jsonwebtoken.JwtException;
import ru.spb.nicetu.newspaper.api.service.exception.EntityExistsException;
import ru.spb.nicetu.newspaper.api.service.exception.ImageStorageException;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;
import ru.spb.nicetu.newspaper.api.service.exception.UnregistredTokenException;
import ru.spb.nicetu.newspaper.api.service.exception.UsernameTakenException;

/**
 * <p>Controller for handling predicted exceptions.</p>
 *
    * @author Nikita Osiptsov
 * @since 1.0
 */
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(NullPointerException.class)
	private ResponseEntity<Map<String, Object>> handleNullPointerExceptionHandler() {
        return getEntity(HttpStatus.BAD_REQUEST, "Entity does not exist");
	}

    @ExceptionHandler(IllegalArgumentException.class)
	private ResponseEntity<Map<String, Object>>  handleIllegalArgumentException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Illegal argument");
	}

    @ExceptionHandler(MissingEntityException.class)
	private ResponseEntity<Map<String, Object>>  handleMissingEntityException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Entity does not exist");
	}

    @ExceptionHandler(ConstraintViolationException.class)
	private ResponseEntity<Map<String, Object>>  handleConstraintViolationException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Invalid data passed");
	}

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpiredException() {
        return getEntity(HttpStatus.FORBIDDEN, "Refresh token expired");
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameTakenException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Username taken");
    }

    @ExceptionHandler(UnregistredTokenException.class)
    public ResponseEntity<Map<String, Object>> handleUnregistredTokenException() {
        return getEntity(HttpStatus.UNAUTHORIZED, "Token is not registered");
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEntityExistsException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Entity exists already");
    }

    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<Map<String, Object>> handleImageStorageException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Bad image passed");
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, Object>> handleMultipartException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Multipart resolution failed. Request is not multipart");
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleFileSizeLimitExceededException() {
        return getEntity(HttpStatus.BAD_REQUEST, "Size of file exceeds limit");
    }

    private ResponseEntity<Map<String, Object>> getEntity(HttpStatus status, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", status.value());
        result.put("error", status.getReasonPhrase());
        result.put("message", message);

        return new ResponseEntity<Map<String, Object>>(result, status);
    }

}
