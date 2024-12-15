package com.duaduatib.eduforum;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MhsDao {

    @Insert
    void registerMhs(EntitasMhs entitasMhs);

    @Query("SELECT * from mahasiswa where usernameMhs=(:usernameMhs) and passwordMhs=(:passwordMhs)")
    EntitasMhs login(String usernameMhs, String passwordMhs);
}
