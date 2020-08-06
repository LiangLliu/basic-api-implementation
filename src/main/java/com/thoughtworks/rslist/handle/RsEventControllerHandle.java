package com.thoughtworks.rslist.handle;

import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RsEventControllerHandle {

    @ExceptionHandler({InvalidIndexException.class,
            InvalidRequestParamException.class,
            MethodArgumentNotValidException.class,
            UserNotFoundException.class})
    public ResponseEntity exceptionHandler(Exception exception) {

        String message;
        CommonError commonError = new CommonError();

        log.error("{}.error :  {}", exception.getClass(), exception.getMessage());

        if (exception instanceof MethodArgumentNotValidException) {
            message = "invalid param";
        } else {
            message = exception.getMessage();
        }

        commonError.setError(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}
