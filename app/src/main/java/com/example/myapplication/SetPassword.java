package com.example.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class SetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public SharedPreferences mSharedPreferences;

    public void setPassword(View view) throws InvalidKeySpecException, NoSuchAlgorithmException {
        TextView newPass = (TextView)
                findViewById(R.id.newPass);
        String password = newPass.getText().toString();
        setNewPass(password);
        MainActivity.hashKey = PBKDF2.createHash(password, PBKDF2.fromHex(getSharedPreferences("secrets", 0).getString("CipherSalt", "")));
        Intent notebookWindow = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(notebookWindow);


    }

    public void setNewPass(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final int SALT_BYTES = 24;
        SecureRandom random = new SecureRandom();
        byte[] pass_salt = new byte[SALT_BYTES];
        random.nextBytes(pass_salt);
        byte[] cipher_salt = new byte[SALT_BYTES];
        random.nextBytes(cipher_salt);
        String hash = PBKDF2.createHash(password, pass_salt);
        mSharedPreferences = getSharedPreferences("secrets", 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("PassSalt", PBKDF2.toHex(pass_salt));
        editor.putString("CipherSalt", PBKDF2.toHex(cipher_salt));
        editor.putString("PassHash", hash);
        editor.apply();

    }

}

