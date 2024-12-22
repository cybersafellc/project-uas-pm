package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class welcome_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnLogin, btnLoginGoogle;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        // Inisialisasi komponen
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        textRegister = findViewById(R.id.daftarAkunTxt);

        // Aksi ketika tombol Login ditekan
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),login_page.class);
                startActivity(i);
            }
        });

        // Aksi ketika tombol Login dengan Google ditekan
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Aksi ketika teks Daftar ditekan
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),choose_role_page.class);
                startActivity(i);
            }
        });
    }
}
