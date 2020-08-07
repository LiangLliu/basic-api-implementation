package com.thoughtworks.rslist.exception;

import com.thoughtworks.rslist.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RsListAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonError> baseExceptionHandler(Exception exception) {

        log.error("{}.error :  {}", exception.getClass(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonError.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonError> methodArgumentNotValidExceptionHandler(Exception exception) {
        log.error("{}.error :  {}", exception.getClass(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonError.builder().error("invalid param").build());
    }

}
