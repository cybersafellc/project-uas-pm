package com.duaduatib.eduforum;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DosenDao {

    @Insert
    void registerDosen(EntitasDosen entitasDosen);

    @Query("SELECT * from dosen where usernameDosen=(:usernameDosen) and passwordDosen=(:passwordDosen)")
    EntitasDosen login(String usernameDosen, String passwordDosen);

}
