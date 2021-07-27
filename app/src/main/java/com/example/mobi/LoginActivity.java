package com.example.mobi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private TextView goToRegister;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        goToRegister = findViewById(R.id.goToRegister);

        loginButton.setOnClickListener(v -> {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();

            if(TextUtils.isEmpty(emailValue)) {
                email.setError("Email is required.");
                return;
            }

            if(TextUtils.isEmpty(passwordValue)) {
                password.setError("Password is required.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), LoggedPageActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to Log in.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });;
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }
}