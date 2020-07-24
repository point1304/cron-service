package com.ksyim.hellocron.server.entity.response;

public class SuccessResponse implements ApiResponse {

    private final boolean success = true;
    private final String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    @Override
    public boolean getSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
