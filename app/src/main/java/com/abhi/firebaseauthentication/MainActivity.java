package com.abhi.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    Intent signInIntent;
    ActivityResultLauncher<Intent> signInLauncher;

    Button logout, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btn_login);
        logout = findViewById(R.id.btn_logout);

        providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        checkResultStatus(result);
                    }
                }
        );
    }

    private void checkResultStatus(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK) {
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            //Toast.makeText(this, response.getProviderType(), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, response.getError().toString(), Toast.LENGTH_SHORT).show();
    }

    public void login(View view) {
        signInLauncher.launch(signInIntent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        logout.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }
}








