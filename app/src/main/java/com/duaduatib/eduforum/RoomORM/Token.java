package com.duaduatib.eduforum.RoomORM;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tokens")
public class Token {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String accessToken;

    // Konstruktor default (tetap ada jika diperlukan)
    public Token() {
    }

    // Konstruktor baru untuk menerima accessToken
    public Token(String access_Token) {
        this.accessToken = access_Token;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
