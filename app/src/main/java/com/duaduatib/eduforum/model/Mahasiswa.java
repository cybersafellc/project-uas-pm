package com.duaduatib.eduforum.model;

public class Mahasiswa {
    private String username;
    private String password;
    private String full_name;
    private String nidn_or_nim;
    private String nama_perguruan_tinggi;

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getNidn_or_nim() {
        return nidn_or_nim;
    }

    public void setNidn_or_nim(String nidn_or_nim) {
        this.nidn_or_nim = nidn_or_nim;
    }

    public String getNama_perguruan_tinggi() {
        return nama_perguruan_tinggi;
    }

    public void setNama_perguruan_tinggi(String nama_perguruan_tinggi) {
        this.nama_perguruan_tinggi = nama_perguruan_tinggi;
    }
}
