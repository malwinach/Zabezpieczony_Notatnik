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
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class ChangePasswordActivity extends AppCompatActivity {

    public ChangePasswordActivity() throws NoSuchProviderException, NoSuchAlgorithmException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
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

    private SharedPreferences mSharedPreferences;
    private SharedPreferences nSharedPreferences;
    MCrypt mcrypt = new MCrypt();

    public void changePassword(View view) throws Exception {
        TextView getPassword = (TextView)
                findViewById(R.id.currentPassword);
        String currentPassword = getPassword.getText().toString();
        if(LoginActivity.validatePass(currentPassword)) {
            String note = mcrypt.HexToASCII(mcrypt.byteArrayToHexString(mcrypt.decrypt(getSharedPreferences("notebook", 0).getString("NotebookContent", ""))));
            TextView newPass = (TextView)
                    findViewById(R.id.newPassword);
            String password = newPass.getText().toString();
            TextView repeatNewPass = (TextView)
                    findViewById(R.id.repeat);
            String repeatPassword = repeatNewPass.getText().toString();
            if(password.equals(repeatPassword)) {
                final int SALT_BYTES = 24;
                SecureRandom random = new SecureRandom();
                byte[] pass_salt = new byte[SALT_BYTES];
                random.nextBytes(pass_salt);
                byte[] cipher_salt = new byte[SALT_BYTES];
                random.nextBytes(cipher_salt);
                String hash = PBKDF2.createHash(password, pass_salt);
                LoginActivity.hash = hash;
                LoginActivity.salt = pass_salt;
                MainActivity.hashKey = PBKDF2.createHash(password, cipher_salt);
                String encryptedNote = mcrypt.byteArrayToHexString(mcrypt.encrypt(note));
                mSharedPreferences = getSharedPreferences("secrets", 0);
                nSharedPreferences = getSharedPreferences("notebook", 0);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                SharedPreferences.Editor notebook = nSharedPreferences.edit();
                editor.putString("PassSalt", PBKDF2.toHex(pass_salt));
                editor.putString("CipherSalt", PBKDF2.toHex(cipher_salt));
                editor.putString("PassHash", hash);
                notebook.putString("NotebookContent", encryptedNote);
                editor.apply();
                notebook.apply();
                Intent notebookWindow = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(notebookWindow);
            }
            else {
                Toast myToast = Toast.makeText(this, "Repeat new password",
                        Toast.LENGTH_SHORT);
                myToast.show();}
            }
        else {
            Toast myToast = Toast.makeText(this, "Incorrect password",
                    Toast.LENGTH_SHORT);
            myToast.show();}
    }

}
