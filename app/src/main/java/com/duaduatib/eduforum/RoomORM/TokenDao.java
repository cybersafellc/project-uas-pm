package com.duaduatib.eduforum.RoomORM;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TokenDao {
    @Insert
    void insert(Token token);

    @Query("SELECT * FROM tokens ORDER BY id DESC LIMIT 1")
    Token getLastToken();

    @Query("DELETE FROM tokens")
    void deleteAllTokens();
}
