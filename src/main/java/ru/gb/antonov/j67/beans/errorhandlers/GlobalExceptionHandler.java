package ru.gb.antonov.j67.beans.errorhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler
    public ResponseEntity<?> catchResouceNotFoundException (ResourceNotFoundException e)
    {
        return new ResponseEntity<>(new ErrorMessage (e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchProductUpdatingException (ProductUpdatingException e)
    {
        return new ResponseEntity<>(new ErrorMessage (e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
