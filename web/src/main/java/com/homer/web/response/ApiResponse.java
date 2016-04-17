package com.homer.web.response;

/**
 * Created by arigolub on 4/17/16.
 */
public class ApiResponse {
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
