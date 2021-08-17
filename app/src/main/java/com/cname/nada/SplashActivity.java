package com.cname.nada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cname.nada.functions.UserID;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.FileInputStream;
import java.io.IOException;

import static com.cname.nada.functions.CheckInternetState.isConnected;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isConnected(getApplicationContext());

        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);

        FileInputStream inFs;
        try {
            inFs = openFileInput("userId.txt");
            byte[] txt = new byte[50];
            inFs.read(txt);
            inFs.close();
            UserID.setUserId((new String(txt)).trim());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } catch (IOException e) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

    }
}