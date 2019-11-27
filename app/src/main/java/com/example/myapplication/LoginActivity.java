package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class LoginActivity extends AppCompatActivity {

    public static String hash;
    public static byte[] salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void logIn(View view) throws InvalidKeySpecException, NoSuchAlgorithmException {
        hash = getSharedPreferences("secrets", 0).getString("PassHash", "");
        salt = PBKDF2.fromHex(getSharedPreferences("secrets", 0).getString("PassSalt", ""));
        TextView passwordInput = (TextView)
                findViewById(R.id.password_input);
        String password = passwordInput.getText().toString();
        if (validatePass(password)) {
            MainActivity.hashKey = PBKDF2.createHash(password, PBKDF2.fromHex(getSharedPreferences("secrets", 0).getString("CipherSalt", "")));
            Intent notebookWindow = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(notebookWindow);
        } else {
            Toast myToast = Toast.makeText(this, "Incorrect password",
                    Toast.LENGTH_SHORT);
            myToast.show();
        }
    }

    public static boolean validatePass(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String hashedPass = PBKDF2.createHash(password, salt);
        if (hashedPass.equals(hash)) {
            return true;
        }
        return false;
    }

}
