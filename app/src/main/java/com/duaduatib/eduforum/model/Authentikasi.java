package com.duaduatib.eduforum.model;

public class Authentikasi {

    public String username;
    public String password;
    public String access_token;
    public Data data;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Data getData() {
        return data;
    }
}
