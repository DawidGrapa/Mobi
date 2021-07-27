package com.example.mobi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.security.spec.ECField;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;
    private Button register;
    private TextView goToLogin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailInputRegister);
        password = findViewById(R.id.passwordInputRegister);
        register = findViewById(R.id.registerButton);
        goToLogin = findViewById(R.id.goToLogin);
        progressBar = findViewById(R.id.registerProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                firebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), LoggedPageActivity.class));
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(RegisterActivity.this, "Account with these email exists.", Toast.LENGTH_SHORT).show();
                            }catch (FirebaseAuthWeakPasswordException toWeakPassword) {
                                Toast.makeText(RegisterActivity.this, "Password is to weak.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}