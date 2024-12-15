package com.duaduatib.eduforum;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EntitasDosen.class,EntitasMhs.class}, version = 1)
public abstract class DatabaseEduForum extends RoomDatabase {

    private static final String dbName = "eduForum";
    private static DatabaseEduForum databaseEduForum;

    public static synchronized DatabaseEduForum getDatabaseEduForum(Context context) {

        if (databaseEduForum == null) {
            databaseEduForum = Room.databaseBuilder(context, DatabaseEduForum.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseEduForum;
    }

    public abstract DosenDao dosenDao();
    public abstract MhsDao mhsDao();
}
