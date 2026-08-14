package com.example.chatapplication.view.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.common.Common;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityProfileBinding;
import com.example.chatapplication.view.display.ViewImageActivity;
import com.example.chatapplication.view.startup.SplashScreenActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager sessionManager;
    private BottomSheetDialog bottomSheetDialog, bsDialogEditName;
    private ProgressDialog progressDialog;

    private static final int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sessionManager = SessionManager.getInstance(this);
        progressDialog = new ProgressDialog(this);

        loadProfileData();
        initActionClick();
    }

    private void loadProfileData() {
        binding.tvUsername.setText(sessionManager.getUserName());
        binding.tvPhone.setText(sessionManager.getUserPhone());
        String imageProfile = sessionManager.getUserImage();

        if (imageProfile != null && !imageProfile.isEmpty()) {
            Glide.with(ProfileActivity.this).load(imageProfile).into(binding.imageProfile);
        }
    }

    private void initActionClick() {
        binding.fabCamera.setOnClickListener(v -> showBottomSheetPickPhoto());

        binding.InEditName.setOnClickListener(v -> showBottomSheetEditName());

        binding.imageProfile.setOnClickListener(v -> {
            Drawable dr = binding.imageProfile.getDrawable();
            if (dr instanceof BitmapDrawable) {
                Common.IMAGE_BITMAP = ((BitmapDrawable) dr).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ProfileActivity.this, binding.imageProfile, "image");
                Intent intent = new Intent(ProfileActivity.this, ViewImageActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });
    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pick, null);

        view.findViewById(R.id.In_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
            }
        });
        view.findViewById(R.id.In_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Camera option coming soon", Toast.LENGTH_SHORT).show();
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (bottomSheetDialog.getWindow() != null) {
                bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

        bottomSheetDialog.show();
    }

    private void showBottomSheetEditName() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name, null);

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bsDialogEditName != null) bsDialogEditName.dismiss();
            }
        });

        EditText edUserName = view.findViewById(R.id.ed_username);
        edUserName.setText(sessionManager.getUserName());

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edUserName.getText().toString().trim();
                if (TextUtils.isEmpty(newName)) {
                    Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    updateName(newName);
                    if (bsDialogEditName != null) bsDialogEditName.dismiss();
                }
            }
        });

        bsDialogEditName = new BottomSheetDialog(this);
        bsDialogEditName.setContentView(view);
        bsDialogEditName.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.imageProfile.setImageBitmap(bitmap);
                sessionManager.updateUserProfile(sessionManager.getUserName(), sessionManager.getUserBio(), imageUri.toString());
                Toast.makeText(this, "Profile picture updated locally", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateName(String newName) {
        sessionManager.updateUserProfile(newName, sessionManager.getUserBio(), sessionManager.getUserImage());
        binding.tvUsername.setText(newName);
        Toast.makeText(getApplicationContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void showDialogSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Log out");
        builder.setMessage("Are you sure you want to log out from Discreet?");
        builder.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                sessionManager.logout();
                Intent intent = new Intent(ProfileActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}