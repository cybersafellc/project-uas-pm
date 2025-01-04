package com.duaduatib.eduforum;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class profil_user extends AppCompatActivity {

    private ImageButton tombolKembali;
    private EditText editUsername, editNamaLengkap, editNIM, editAsalPT;
    private ImageView fotoProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_user);
    }
}
