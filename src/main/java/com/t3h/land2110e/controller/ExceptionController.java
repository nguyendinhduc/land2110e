package com.t3h.land2110e.controller;

import com.t3h.land2110e.model.exception.ResponseObException;
import com.t3h.land2110e.model.response.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//bat exception
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Object> handlerResponseException(ResponseException ex) {
        return new ResponseEntity<>(ResponseObException.build(ex),
                ex.getHttpCode());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handlerException(Exception ex) {
//        return new ResponseEntity<>(ob,
//                ex.getHttpCode());
//    }
}
