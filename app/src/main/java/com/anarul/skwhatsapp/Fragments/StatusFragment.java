package com.anarul.skwhatsapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anarul.skwhatsapp.Adapters.StatusAdapter;
import com.anarul.skwhatsapp.Models.Status;
import com.anarul.skwhatsapp.databinding.FragmentStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusFragment extends Fragment implements StatusAdapter.StatusDeleteListener {

    private FragmentStatusBinding binding;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ArrayList<Status> statusList;
    private StatusAdapter statusAdapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "StatusFragment";

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView: Fragment view created");

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        statusList = new ArrayList<>();

        Log.d(TAG, "onCreateView: Firebase and ArrayList initialized");

        setupStatusRecyclerView();
        setupProfileImage();

        binding.btnStatus.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: Status button clicked");
            pickImage();
        });

        return binding.getRoot();
    }

    private void setupStatusRecyclerView() {
        statusAdapter = new StatusAdapter(statusList, getContext(), this);
        binding.recyclerViewStatus.setAdapter(statusAdapter);
        binding.recyclerViewStatus.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupProfileImage() {
        StorageReference profileRef = storage.getReference().child("profile_pictures").child(auth.getCurrentUser().getUid());
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(binding.profileStatus));
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        String userId = auth.getCurrentUser().getUid();
        StorageReference statusRef = storage.getReference().child("status_pictures").child(userId).child(System.currentTimeMillis() + ".jpg");

        statusRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            statusRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference statusRefDb = database.getReference().child("Status").child(userId);
                String statusId = statusRefDb.push().getKey();
                Status status = new Status(statusId, uri.toString(), System.currentTimeMillis());
                statusRefDb.child(statusId).setValue(status);
                statusList.add(status);
                statusAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Status uploaded", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to upload status", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDeleteStatus(Status status) {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference statusRefDb = database.getReference().child("Status").child(userId).child(status.getStatusId());
        statusRefDb.removeValue().addOnSuccessListener(aVoid -> {
            statusList.remove(status);
            statusAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Status deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to delete status", Toast.LENGTH_SHORT).show();
        });
    }
}
