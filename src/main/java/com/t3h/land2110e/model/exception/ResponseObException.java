package com.t3h.land2110e.model.exception;

import com.t3h.land2110e.model.response.ResponseException;

public class ResponseObException {
    private String message;
    private int status;

    public ResponseObException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static ResponseObException build(ResponseException ex) {
        return new ResponseObException(ex.getMessage(), ex.getStatus());
    }
}
