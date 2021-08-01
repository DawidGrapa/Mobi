package com.example.mobi.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobi.LoggedIn;
import com.example.mobi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private TextView goToRegister;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {

            setContentView(R.layout.activity_login);

            email = findViewById(R.id.emailInput);
            password = findViewById(R.id.passwordInput);
            loginButton = findViewById(R.id.loginButton);
            progressBar = findViewById(R.id.loginProgressBar);
            goToRegister = findViewById(R.id.goToRegister);
            firebaseAuth = FirebaseAuth.getInstance();

            loginButton.setOnClickListener(v -> {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                if (TextUtils.isEmpty(emailValue)) {
                    email.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(passwordValue)) {
                    password.setError("Password is required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                closeKeyboard();

                firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), LoggedIn.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            });

            goToRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), LoggedIn.class));
            finish();
        }
    }

    private void closeKeyboard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}