package com.eleks.placescanner.common.exception;

import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.common.exception.domain.InvalidRequestException;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorMessage(404, new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = UnexpectedResponseException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage unexpectedResponseException(UnexpectedResponseException ex, WebRequest request) {
        return new ErrorMessage(500, new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage invalidRequestException(InvalidRequestException ex, WebRequest request) {
        return new ErrorMessage(400, new Date(), ex.getMessage(), request.getDescription(false));
    }
}
