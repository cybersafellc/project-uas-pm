package com.duaduatib.eduforum;

public class DataClass {
    public String dataTitle;
    public int dataDesc;
    public String dataLang;
    public int dataImage;

    public String getDataTitle() {
        return dataTitle;
    }

    public int getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }

    public int getDataImage() {
        return dataImage;
    }

    public DataClass(String dataTitle, int dataDesc, String dataLang, int dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataImage = dataImage;
    }
}