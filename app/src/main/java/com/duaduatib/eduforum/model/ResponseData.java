package com.duaduatib.eduforum.model;

import java.util.List;

public class ResponseData {
    private int status;
    private String message;
    private Data data;

    // Getter dan Setter
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
