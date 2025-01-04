package com.duaduatib.eduforum.model;

public class ApiResponse {
    private int status;
    private String message;
    private Data data;
    private boolean error;


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }
}
