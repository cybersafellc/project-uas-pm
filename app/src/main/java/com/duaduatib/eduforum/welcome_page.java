package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class welcome_page extends AppCompatActivity {

    // Declare UI components
    private Button btnLogin, btnLoginGoogle;
    private TextView textRegister;

    // Declare GoogleSignInClient
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        // Initialize UI components
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        textRegister = findViewById(R.id.daftarAkunTxt);

        // Configure Google Sign-In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))// Updated Client ID
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);

        // Set OnClickListeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), login_page.class);
                startActivity(i);
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), choose_role_page.class);
                startActivity(i);
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            try {
                task.getResult(ApiException.class);
                onStart();
                if (data == null) {
                    Log.e("GoogleSignIn", "Sign-in failed: Intent data is null");
                }
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Sign-in failed. Code: " + e.getStatusCode() + ", Message: " + e.getMessage(), e);

                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Google Sign-In was successful
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Update the UI with the signed-in user's information
            if (account != null) {
                String idToken = account.getIdToken();
                if (idToken != null) {
                    postIdTokenToServer(idToken);
                } else {
                    Toast.makeText(this, "ID Token is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            // Google Sign-In failed, update UI accordingly
            Toast.makeText(this, "Google sign-in failed.", Toast.LENGTH_SHORT).show();
            Log.e("GoogleSignIn", "Sign-in failed. Code: " + e.getStatusCode() + ", Message: " + e.getMessage(), e);
        }
    }

    private void postIdTokenToServer(String idToken) {
        // URL of the external server
        String url = "https://00ef-103-111-100-233.ngrok-free.app/users/oauth/google";

        // Create a JSON object to send
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idToken", idToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send POST request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle server response
                        Toast.makeText(welcome_page.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Log.d("ServerResponse", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(welcome_page.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", error.toString());
                    }
                });

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }
}
