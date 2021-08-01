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

import com.example.mobi.user.DAOUser;
import com.example.mobi.LoggedIn;
import com.example.mobi.R;
import com.example.mobi.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, name;
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
        name = findViewById(R.id.nameInputRegister);
        register = findViewById(R.id.registerButton);
        goToLogin = findViewById(R.id.goToLogin);
        progressBar = findViewById(R.id.registerProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                String nameValue = name.getText().toString();

                if(TextUtils.isEmpty(nameValue)) {
                    name.setError("Name is required.");
                    return;
                }

                if(TextUtils.isEmpty(emailValue)) {
                    email.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(passwordValue)) {
                    password.setError("Password is required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                closeKeyboard();

                firebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            addUserToDatabase(nameValue, emailValue);
                            startActivity(new Intent(getApplicationContext(), LoggedIn.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void addUserToDatabase(String nameValue, String emailValue) {
        User user = new User(nameValue, emailValue);
        DAOUser daoUser = new DAOUser();
        daoUser.add(user);
        daoUser.changeLogin(nameValue);
    }

}