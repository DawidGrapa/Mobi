package com.example.mobi.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userSettings extends AppCompatActivity {

    Button save;
    EditText name, desc, age;
    RadioGroup sex;
    RadioButton male, female, radioButton;
    FirebaseDatabase db;
    User user;
    DAOUser daoUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        daoUser = new DAOUser();

        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
                name = findViewById(R.id.userName);
                desc = findViewById(R.id.userDesc);
                age = findViewById(R.id.userAge);
                sex = findViewById(R.id.userSex);

                male = findViewById(R.id.male);
                female = findViewById(R.id.female);

                if(!TextUtils.isEmpty(user.getSex())) {
                    if(user.getSex().equals("Male")) {
                        sex.check(male.getId());
                    } else {
                        sex.check(female.getId());
                    }
                }

                name.setText(user.getFirstName());
                desc.setText(user.getDescription());
                age.setText(user.getAge());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        save = findViewById(R.id.saveData);

        db = FirebaseDatabase.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString();
                String descValue = desc.getText().toString();
                String ageValue = age.getText().toString();
                int radioId = sex.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                if (radioButton == null) {
                    Toast.makeText(userSettings.this, "Sex is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sexValue = radioButton.getText().toString();

                if(TextUtils.isEmpty(nameValue)) {
                    name.setError("Name is required.");
                    return;
                }

                if(TextUtils.isEmpty(descValue)) {
                    desc.setError("Description is required");
                    return;
                }

                if(TextUtils.isEmpty(ageValue)) {
                    age.setError("Age is required");
                    return;
                }

                user.setFirstName(nameValue);
                user.setAge(ageValue);
                user.setSex(sexValue);
                user.setDescription(descValue);

                FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).setValue(user);
                finish();
            }
        });

    }
}