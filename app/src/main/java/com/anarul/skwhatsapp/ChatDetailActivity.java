package com.anarul.skwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anarul.skwhatsapp.Adapters.ChatAdapter;
import com.anarul.skwhatsapp.Models.MessageModel;
import com.anarul.skwhatsapp.Models.Users;
import com.anarul.skwhatsapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();

        // Receiver details
        String receiverId = getIntent().getStringExtra("userId");
        String receiverName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilepic");

        binding.userName.setText(receiverName);
          Picasso.get().load(profilePic).placeholder(R.drawable.ic_profile).into(binding.profileImage);
         binding.backArrow.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
          Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
        startActivity(intent);
        }
        });


        long appID = 966106929;
        String appSign = "7ea55762617b8d5c02b8b96db760a3dcacbfd34afcdc2991c5d6ca511b32d15e";
        fetchSenderNameAndInitService(appID, appSign,senderId,receiverId,receiverName);



//        long appID = 966106929;
//        String appSign = "7ea55762617b8d5c02b8b96db760a3dcacbfd34afcdc2991c5d6ca511b32d15e";
//        initCallInviteService(appID, appSign, senderId,senderName);

//        initVoiceButton(receiverId, receiverName);
//
//        initVideoButton(receiverId, receiverName);

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, receiverId);
        binding.chatRecyclarView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclarView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    model.setMessageId(snapshot1.getKey());
                    messageModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etMessage.getText().toString().isEmpty()) {
                    binding.etMessage.setError("Please Enter Any Message");
                    return;
                }
                String message = binding.etMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.etMessage.setText("");

                database.getReference().child("Chats").child(senderRoom).push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Chats").child(receiverRoom).push().setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
    public void fetchSenderNameAndInitService(final long appID, final String appSign,String senderId,String receiverId,String receiverName) {
        // Fetch sender name from the database
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        if (users != null) {
                            String senderName = users.getUserName();
                            // Call initCallInviteService with the fetched sender name
                            initCallInviteService(appID, appSign, senderId, senderName);
                            initVoiceButton(receiverId, receiverName);
                            initVideoButton(receiverId, receiverName);
                        } else {
                            // Handle case where user is null
                            Log.e("ChatDetailActivity", "User is null");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Log.e("ChatDetailActivity", "Database error: " + error.getMessage());
                    }
                });
    }
    public void initCallInviteService(long appID, String appSign, String userID, String userName) {
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,
                callInvitationConfig);
    }

    private void initVideoButton(String receiverId, String receiverName) {
        ZegoSendCallInvitationButton newVideoCall = findViewById(R.id.video_CallBtn);
        newVideoCall.setIsVideoCall(true);
        newVideoCall.setOnClickListener(v -> {
           // String[] split = receiverId.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
           // for (String userID : split) {
                users.add(new ZegoUIKitUser(receiverId, receiverName));
           // }
            newVideoCall.setInvitees(users);
        });
    }

    private void initVoiceButton(String receiverId, String receiverName) {
        ZegoSendCallInvitationButton newVoiceCall = findViewById(R.id.voice_CallBtn);
        newVoiceCall.setIsVideoCall(false);
        newVoiceCall.setOnClickListener(v -> {
           // String[] split = receiverId.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
           // for (String userID : split) {
                users.add(new ZegoUIKitUser(receiverId, receiverName));
           // }
            newVoiceCall.setInvitees(users);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}