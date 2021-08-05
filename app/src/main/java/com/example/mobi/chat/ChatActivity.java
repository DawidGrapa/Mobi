package com.example.mobi.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobi.R;
import com.example.mobi.user.DAOUser;
import com.example.mobi.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference usersDB, chatForUser, chatDB;
    FirebaseAuth mAuth;
    String currentUid, pairImage, pairID, chatID;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<Chat> chatList;
    EditText message;
    ImageButton send;
    TextView userName;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatList = new ArrayList<>();

        recyclerView = findViewById(R.id.chatRecyclerView);



        message = findViewById(R.id.messageValue);
        send = findViewById(R.id.sendMessae);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        usersDB = db.getReference().child(User.class.getSimpleName());
        currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        pairID = getIntent().getExtras().getString("pairID");
        pairImage = getIntent().getExtras().getString("pairImage");

        chatForUser = usersDB.child(currentUid).child("Connections").child("Matches").child(pairID).child("ChatID");


        manager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(manager);
        adapter = new ChatAdapter(chatList, ChatActivity.this);

        recyclerView.setAdapter(adapter);
        chatDB = db.getReference().child("Chat");

        setSendOnClick();
        getChatID();
        setUserData();
    }

    private void setUserData() {
        userName = findViewById(R.id.chatUserName);
        userImage = findViewById(R.id.chatUserImage);

        DAOUser.usersDB.child(pairID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    User newUser = snapshot.getValue(User.class);
                    userName.setText(newUser.getFirstName());
                    if (newUser.getImageUri() == null && newUser.getSex() == null) {
                        userImage.setImageResource(R.drawable.ic_user);
                    } else if (newUser.getSex().equals("Male") && newUser.getImageUri() == null) {
                        userImage.setImageResource(R.drawable.ic_man);
                    } else if (newUser.getSex().equals("Female") && newUser.getImageUri() == null) {
                        userImage.setImageResource(R.drawable.ic_woman);
                    } else if (newUser.getImageUri() != null){
                        Glide.with(ChatActivity.this).load(newUser.getImageUri()).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSendOnClick() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void getChatID() {
        chatForUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    chatID = snapshot.getValue().toString();
                    chatDB = chatDB.child(chatID);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        chatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String message = null;
                    String sentBy = null;
                    if(snapshot.child("message").getValue() != null) {
                        message = snapshot.child("message").getValue().toString();
                    }
                    if(snapshot.child("sentByUser").getValue() != null) {
                        sentBy = snapshot.child("sentByUser").getValue().toString();
                    }

                    Chat chat = new Chat(message, sentBy.equals(currentUid));
                    chatList.add(chat);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatList.size()-1);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendMessage() {
        String messageText  = message.getText().toString();

        if(!TextUtils.isEmpty(messageText)){
            DatabaseReference newMessageDB = chatDB.push();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", messageText);
            hashMap.put("sentByUser", currentUid);
            newMessageDB.setValue(hashMap);
        }
        message.setText(null);
    }

    public void goBack(View view) {
        finish();
    }
}