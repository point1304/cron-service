package com.ksyim.hellocron.server.entity.response;

public class ErrorResponse implements ApiResponse {

    private static final boolean success = false;
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public boolean getSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
