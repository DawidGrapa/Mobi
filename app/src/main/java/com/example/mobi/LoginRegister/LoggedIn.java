package com.example.mobi.LoginRegister;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.mobi.LoginRegister.LoginActivity;
import com.example.mobi.R;
import com.example.mobi.databinding.ActivityLoggedInBinding;
import com.example.mobi.user.DAOUser;
import com.example.mobi.user.User;
import com.example.mobi.user.userSettings;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;


import com.example.mobi.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;


public class LoggedIn extends AppCompatActivity {

    private ActivityLoggedInBinding binding;
    private FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    private Uri uri;
    private StorageTask uploadTask;
    private static final int IMAGE_REQUEST = 1;
    User user;
    DAOUser daoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            startApp();

        } else {
            finishApp();
        }

    }

    private void finishApp() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void startApp() {
        storageReference = FirebaseStorage.getInstance().getReference().child(User.class.getSimpleName()).child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        daoUser = new DAOUser();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);

        tabs.getTabAt(0).setIcon(R.drawable.ic_user);
        tabs.getTabAt(1).setIcon(R.drawable.ic_fire);
        tabs.getTabAt(2).setIcon(R.drawable.ic_speak);

        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void userSettings(View view) {
        startActivity(new Intent(getApplicationContext(), userSettings.class));
    }

    public void changeImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if(uri != null) {
            uploadTask = storageReference.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    } else {
                        return storageReference.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        user.setImageUri(mUri);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageUri", user.getImageUri());
                        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).updateChildren(hashMap);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            if(uploadTask!=null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Upload in progress.", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }

    }
}