package com.thoughtworks.rslist.handle;

import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandle {

    @ExceptionHandler(InvalidIndexException.class)
    public ResponseEntity exceptionHandler(InvalidIndexException invalidIndexException) {
        CommonError commonError = new CommonError();
        commonError.setError(invalidIndexException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}
