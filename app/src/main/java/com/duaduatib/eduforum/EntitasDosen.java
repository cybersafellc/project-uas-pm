package com.duaduatib.eduforum;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dosen")
public class EntitasDosen {
    @PrimaryKey(autoGenerate = true)
    Integer dosenId;

    @ColumnInfo(name = "usernameDosen")
    String usernameDosen;

    @ColumnInfo(name = "passwordDosen")
    String passwordDosen;

    @ColumnInfo(name = "namaLengkapDosen")
    String namaLengkapDosen;

    @ColumnInfo(name = "NIDN")
    String NIDN;

    @ColumnInfo(name = "perguruanTinggiDosen")
    String perguruanTinggiDosen;

    public Integer getDosenId() {
        return dosenId;
    }

    public void setDosenId(Integer dosenId) {
        this.dosenId = dosenId;
    }

    public String getUsernameDosen() {
        return usernameDosen;
    }

    public void setUsernameDosen(String usernameDosen) {
        this.usernameDosen = usernameDosen;
    }

    public String getPasswordDosen() {
        return passwordDosen;
    }

    public void setPasswordDosen(String passwordDosen) {
        this.passwordDosen = passwordDosen;
    }

    public String getNamaLengkapDosen() {
        return namaLengkapDosen;
    }

    public void setNamaLengkapDosen(String namaLengkapDosen) {
        this.namaLengkapDosen = namaLengkapDosen;
    }

    public String getNIDN() {
        return NIDN;
    }

    public void setNIDN(String NIDN) {
        this.NIDN = NIDN;
    }

    public String getPerguruanTinggiDosen() {
        return perguruanTinggiDosen;
    }

    public void setPerguruanTinggiDosen(String perguruanTinggiDosen) {
        this.perguruanTinggiDosen = perguruanTinggiDosen;
    }
}
