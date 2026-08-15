package com.example.chatapplication.view.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityProfileBinding;
import com.example.chatapplication.model.User;
import com.example.chatapplication.viewmodel.ProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel;
    private BottomSheetDialog bottomSheetDialog, bsDialogEditName, bsDialogEditAbout;
    private User currentUser;

    private static final int IMAGE_GALLERY_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnQrCode.setOnClickListener(v ->
                Toast.makeText(this, "Your Encrypted QR Code Card", Toast.LENGTH_SHORT).show()
        );

        observeProfile();
        initActionClick();
    }

    private void observeProfile() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                binding.tvUsername.setText(user.getUserName());
                binding.tvPhone.setText(user.getUserPhone());
                binding.tvAbout.setText(user.getBio() != null && !user.getBio().isEmpty()
                        ? user.getBio()
                        : "Building something awesome 🚀");

                String imageProfile = user.getImageProfile();
                if (imageProfile != null && !imageProfile.isEmpty()) {
                    Glide.with(ProfileActivity.this).load(imageProfile).into(binding.imageProfile);
                } else {
                    binding.imageProfile.setImageResource(R.drawable.icon_person);
                }
            }
        });
    }

    private void initActionClick() {
        binding.fabCamera.setOnClickListener(v -> showBottomSheetPickPhoto());

        binding.InEditName.setOnClickListener(v -> showBottomSheetEditName());

        binding.InEditAbout.setOnClickListener(v -> showBottomSheetEditAbout());

        binding.imageProfile.setOnClickListener(v -> showBottomSheetPickPhoto());
    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pick, null);

        view.findViewById(R.id.In_gallery).setOnClickListener(v -> {
            openGallery();
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
        });

        view.findViewById(R.id.In_camera).setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Camera option coming soon", Toast.LENGTH_SHORT).show();
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showBottomSheetEditName() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name, null);

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            if (bsDialogEditName != null) bsDialogEditName.dismiss();
        });

        EditText edUserName = view.findViewById(R.id.ed_username);
        if (currentUser != null) {
            edUserName.setText(currentUser.getUserName());
        }

        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String newName = edUserName.getText().toString().trim();
            if (TextUtils.isEmpty(newName)) {
                Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
            } else {
                String bio = currentUser != null ? currentUser.getBio() : "";
                String image = currentUser != null ? currentUser.getImageProfile() : "";
                viewModel.updateProfile(newName, bio, image);
                Toast.makeText(getApplicationContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                if (bsDialogEditName != null) bsDialogEditName.dismiss();
            }
        });

        bsDialogEditName = new BottomSheetDialog(this);
        bsDialogEditName.setContentView(view);
        bsDialogEditName.show();
    }

    private void showBottomSheetEditAbout() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name, null);

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        if (tvTitle != null) {
            tvTitle.setText("Edit About & Status");
        }

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            if (bsDialogEditAbout != null) bsDialogEditAbout.dismiss();
        });

        EditText edAbout = view.findViewById(R.id.ed_username);
        edAbout.setHint("About & Status");
        if (currentUser != null) {
            edAbout.setText(currentUser.getBio());
        }

        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String newAbout = edAbout.getText().toString().trim();
            if (!TextUtils.isEmpty(newAbout)) {
                String name = currentUser != null ? currentUser.getUserName() : "";
                String image = currentUser != null ? currentUser.getImageProfile() : "";
                viewModel.updateProfile(name, newAbout, image);
                Toast.makeText(getApplicationContext(), "Status updated", Toast.LENGTH_SHORT).show();
                if (bsDialogEditAbout != null) bsDialogEditAbout.dismiss();
            }
        });

        bsDialogEditAbout = new BottomSheetDialog(this);
        bsDialogEditAbout.setContentView(view);
        bsDialogEditAbout.show();
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
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.imageProfile.setImageBitmap(bitmap);
                String name = currentUser != null ? currentUser.getUserName() : "";
                String bio = currentUser != null ? currentUser.getBio() : "";
                viewModel.updateProfile(name, bio, imageUri.toString());
                Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}