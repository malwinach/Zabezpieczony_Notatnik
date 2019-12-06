package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

public class MainActivity extends AppCompatActivity {

    public static String hashKey;
    public static Key aesKey;
    public SharedPreferences mSharedPreferences;
    MCrypt mCrypt = new MCrypt();
    public static String KEY_ALIAS = "kluczyk";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this.getSharedPreferences("secrets", 0).edit().clear().commit();
            if (getSharedPreferences("secrets", 0).getString("PassHash", "").equals("")) {
                Intent setNewPassword = new Intent(getApplicationContext(), SetPassword.class);
                startActivity(setNewPassword);
            } else {
                if (hashKey == null) {
                    Intent logIn = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logIn);
                }
            }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(hashKey != null) {
            aesKey = generateOrGetKey();
            TextView notebook = (TextView) findViewById(R.id.notebook);
            String encryptedStr = getSharedPreferences("notebook", 0).getString("NotebookContent", "");
            try {
                //String decryptedNote = mCrypt.HexToASCII(mCrypt.byteArrayToHexString(mCrypt.decrypt(encryptedStr)));
                String decryptedNote = aesKey.toString() + "keyspec" + MCrypt.keyspec.toString();
                notebook.setText(decryptedNote);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        TextView notebook = (TextView)
                findViewById(R.id.notebook);
        String note = notebook.getText().toString();
        try {
            byte[] encryptedNote = MCrypt.encrypt(note);
            mSharedPreferences = getSharedPreferences("notebook", 0);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            String encryptedStr = MCrypt.byteArrayToHexString(encryptedNote);
            editor.putString("NotebookContent", encryptedStr);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Key generateOrGetKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

                return keyGenerator.generateKey();
            } else {
                System.out.println(keyStore.getKey(KEY_ALIAS, null).toString());
                return keyStore.getKey(KEY_ALIAS, null);
            }
        }
        catch (KeyStoreException e) {
            Toast.makeText(this, "KEY STORE EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (CertificateException e) {
            Toast.makeText(this, "CERTIFICATE EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(this, "IO EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "NO SUCH ALGORITHM EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (NoSuchProviderException e) {
            Toast.makeText(this, "NO SUCH PROVIDER EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (InvalidAlgorithmParameterException e) {
            Toast.makeText(this, "INVALID ALGORITHM EXCEPTION", Toast.LENGTH_SHORT).show();
        }
        catch (UnrecoverableKeyException e) {
            Toast.makeText(this, "UNRECOVERABLE KEY EXCEPTION", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public void go(View view) {
        Intent logIn = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(logIn);
    }
}

