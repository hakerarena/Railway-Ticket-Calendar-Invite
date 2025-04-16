package com.hakerarena.railway.model;

public class SuccessResponse {
    Boolean success;
    String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SuccessResponse{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
