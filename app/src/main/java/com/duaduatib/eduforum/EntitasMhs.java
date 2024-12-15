package com.duaduatib.eduforum;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mahasiswa")
public class EntitasMhs {
    @PrimaryKey(autoGenerate = true)
    Integer mhsId;

    @ColumnInfo(name = "usernameMhs")
    String usernameMhs;

    @ColumnInfo(name = "passwordMhs")
    String passwordMhs;

    @ColumnInfo(name = "namaLengkapMhs")
    String namaLengkapMhs;

    @ColumnInfo(name = "NIM")
    String NIM;

    @ColumnInfo(name = "perguruanTinggiMhs")
    String perguruanTinggiMhs;

    public Integer getMhsId() {
        return mhsId;
    }

    public void setMhsId(Integer mhsId) {
        this.mhsId = mhsId;
    }

    public String getUsernameMhs() {
        return usernameMhs;
    }

    public void setUsernameMhs(String usernameMhs) {
        this.usernameMhs = usernameMhs;
    }

    public String getPasswordMhs() {
        return passwordMhs;
    }

    public void setPasswordMhs(String passwordMhs) {
        this.passwordMhs = passwordMhs;
    }

    public String getNamaLengkapMhs() {
        return namaLengkapMhs;
    }

    public void setNamaLengkapMhs(String namaLengkapMhs) {
        this.namaLengkapMhs = namaLengkapMhs;
    }

    public String getNIM() {
        return NIM;
    }

    public void setNIM(String NIM) {
        this.NIM = NIM;
    }

    public String getPerguruanTinggiMhs() {
        return perguruanTinggiMhs;
    }

    public void setPerguruanTinggiMhs(String perguruanTinggiMhs) {
        this.perguruanTinggiMhs = perguruanTinggiMhs;
    }
}
