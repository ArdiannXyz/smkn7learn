package com.example.smk7.RecycleTugasGuru;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;

public class UploadTugas_guru extends AppCompatActivity {

    private ImageView backButton;

    private BottomNavigationHandler navigationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_tugas_guru);


        backButton = findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

}
