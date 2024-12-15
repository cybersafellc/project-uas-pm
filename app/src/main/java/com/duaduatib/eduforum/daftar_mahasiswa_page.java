package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class daftar_mahasiswa_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnDaftarMhs;
    private EditText usernameMhs, passwordMhs, namaLengkapMhs;
    private EditText NIM, perguruanTinggiMhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_mahasiswa_page);

        // Inisialisasi komponen
        btnDaftarMhs = findViewById(R.id.btnDaftarMhs);
        usernameMhs = findViewById(R.id.inputUsernameDaftarMhs);
        passwordMhs = findViewById(R.id.inputPasswordDaftarMhs);
        namaLengkapMhs = findViewById(R.id.inputNamaLengkapDaftarMhs);
        NIM = findViewById(R.id.inputNIMDaftarMhs);
        perguruanTinggiMhs = findViewById(R.id.inputPerguruanTinggiDaftarMhs);

        // Aksi ketika tombol Daftar ditekan
        btnDaftarMhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating Mhs Entity
                EntitasMhs entitasMhs =new EntitasMhs();
                entitasMhs.setUsernameMhs(usernameMhs.getText().toString());
                entitasMhs.setPasswordMhs(passwordMhs.getText().toString());
                entitasMhs.setNamaLengkapMhs(namaLengkapMhs.getText().toString());
                entitasMhs.setNIM(NIM.getText().toString());
                entitasMhs.setPerguruanTinggiMhs(perguruanTinggiMhs.getText().toString());
                if (validateInput(entitasMhs)) {
                    //Do insert operation
                    DatabaseEduForum databaseEduForum = DatabaseEduForum.getDatabaseEduForum(getApplicationContext());
                    final MhsDao mhsDao = databaseEduForum.mhsDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Register user
                            mhsDao.registerMhs(entitasMhs);
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

    private Boolean validateInput(EntitasMhs entitasMhs) {
        if (entitasMhs.getUsernameMhs().isEmpty() ||
                entitasMhs.getPasswordMhs().isEmpty() ||
                entitasMhs.getNamaLengkapMhs().isEmpty() ||
                entitasMhs.getNIM().isEmpty() ||
                entitasMhs.getPerguruanTinggiMhs().isEmpty()
        ) {
            return false;
        }
        return true;
    }
}

