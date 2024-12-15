package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class daftar_dosen_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnDaftarDosen;
    private EditText usernameDosen, passwordDosen, namaLengkapDosen;
    private EditText NIDN, perguruanTinggiDosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_dosen_page);

        // Inisialisasi komponen
        btnDaftarDosen = findViewById(R.id.btnDaftarDosen);
        usernameDosen = findViewById(R.id.inputUsernameDaftarDosen);
        passwordDosen = findViewById(R.id.inputPasswordDaftarDosen);
        namaLengkapDosen = findViewById(R.id.inputNamaLengkapDaftarDosen);
        NIDN = findViewById(R.id.inputNIDNDaftarDosen);
        perguruanTinggiDosen = findViewById(R.id.inputPerguruanTinggiDaftarDosen);

        // Aksi ketika tombol Daftar ditekan
        btnDaftarDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating Dosen Entity
                EntitasDosen entitasDosen =new EntitasDosen();
                entitasDosen.setUsernameDosen(usernameDosen.getText().toString());
                entitasDosen.setPasswordDosen(passwordDosen.getText().toString());
                entitasDosen.setNamaLengkapDosen(namaLengkapDosen.getText().toString());
                entitasDosen.setNIDN(NIDN.getText().toString());
                entitasDosen.setPerguruanTinggiDosen(perguruanTinggiDosen.getText().toString());
                if (validateInput(entitasDosen)) {
                    //Do insert operation
                    DatabaseEduForum databaseEduForum = DatabaseEduForum.getDatabaseEduForum(getApplicationContext());
                    final DosenDao dosenDao = databaseEduForum.dosenDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Register user
                            dosenDao.registerDosen(entitasDosen);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(),login_page.class);
                                    startActivity(i);
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Isi semua field data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateInput(EntitasDosen entitasDosen) {
        if (entitasDosen.getUsernameDosen().isEmpty() ||
                entitasDosen.getPasswordDosen().isEmpty() ||
                entitasDosen.getNamaLengkapDosen().isEmpty() ||
                entitasDosen.getNIDN().isEmpty() ||
                entitasDosen.getPerguruanTinggiDosen().isEmpty()
        ) {
            return false;
        }
        return true;
    }
}

