package com.duaduatib.eduforum.model;

public class ProfileResponse {
    private int status;
    private String message;
    private ProfileData data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ProfileData getData() {
        return data;
    }

    public static class ProfileData {
        private String username;
        private String full_name;
        private String nidn_or_nim;
        private String nama_perguruan_tinggi;
        private String profile_url;

        public String getUsername() {
            return username;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getNidn_or_nim() {
            return nidn_or_nim;
        }

        public String getNama_perguruan_tinggi() {
            return nama_perguruan_tinggi;
        }

        public String getProfile_url() {
            return profile_url;
        }
    }
}



